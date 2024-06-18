package task.backend.ch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import task.backend.ch.model.PlaceInfo;
import task.backend.ch.model.PlaceInfoDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class PlaceService {
    private static final Logger log = LoggerFactory.getLogger(PlaceService.class);

    private final WebClient webClient;
    private final ObjectMapper mapper;

    public PlaceService(@Value("${service.url}") String apiUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
        this.mapper = new ObjectMapper();
    }

    public Mono<PlaceInfoDTO> getPlaceSummary(String placeId) {
        return webClient.get()
            .uri("/{placeId}", placeId) // Properly format URI with path variable
            .retrieve()
            .bodyToMono(String.class)
            .flatMap(this::deserializePlaceInfo)
            .map(this::processPlaceInfo)
            .onErrorResume(e -> {
                log.error("Error retrieving place info ", e);
                return Mono.empty();
            });
    }

    private Mono<PlaceInfo> deserializePlaceInfo(String jsonResponse) {
        try {
            PlaceInfo placeInfo = mapper.readValue(jsonResponse, PlaceInfo.class);
            return Mono.just(placeInfo);
        } catch (JsonProcessingException e) {
            log.error("Error deserializing place info", e);
            return Mono.error(e);
        }
    }

    private static final List<String> DAYS_ORDER = Arrays.asList(
            "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday");

    public PlaceInfoDTO processPlaceInfo(PlaceInfo placeInfo) {
        //It seems some place are working from tuesday, or they might have very different working weeks,
        //so here should not rely on a standard week, so we get the working days from the remote service result
        ArrayList<String> days = new ArrayList<>(placeInfo.getOpeningDays().getDays().keySet());
        String currentDay = placeInfo.getOpeningDays().getDays().firstEntry().getKey();
        String lastDay = placeInfo.getOpeningDays().getDays().lastEntry().getKey();
        //this is where we will store the aggregated opening time ranges
        LinkedHashMap<String, List<PlaceInfo.OpeningDays.OpeningTime>> ranges = new LinkedHashMap<>();
        //the remote result list of the working days - LinkedHashMap, so we can retain the ordering, same for the produced ranges
        LinkedHashMap<String, List<PlaceInfo.OpeningDays.OpeningTime>> openingDays = placeInfo.getOpeningDays().getDays();
        for(int i = 0; i < days.size(); i++) {
            //We're passing through every day against the next day until we find a different Opening Time
            // Ex. Monday - Tuesday, Tuesday - Wednesday until we find a day with a different Opening Time,
            // then we add the range from the current day to the day previous of the one with the different Opening Time,
            // because it means that that day is the last with the same Opening Time - thus the range.
            // Ex. Monday is 1st day and the 1st day with different OT is Friday, so range is Monday - Thursday and we add the OT from one of them
            if(openingDays.get(currentDay) != null && !openingDays.get(currentDay).equals(openingDays.get(DAYS_ORDER.get(i+1)))) {
                if(lastDay.equals(days.get(i))) {
                    ranges.put(currentDay + " - " + days.get(i), openingDays.get(currentDay));
                } else {
                    // When we reach the last day we add the last range
                    // Ex. We have Monday - Thursday and then we have Friday and we reach Sunday, so we put second range Friday - Sunday.
                    ranges.put(currentDay + " - " + lastDay, openingDays.get(lastDay));
                }
                //Increase the currentDay to be the new currentDay with different OT and start anew, so we can find the next range.
                currentDay = DAYS_ORDER.get(i+1);
            }
        }

        PlaceInfoDTO dto = new PlaceInfoDTO();
        dto.setName(placeInfo.getName());
        dto.setAddress(placeInfo.getAddress());
        dto.setOpeningDays(new PlaceInfo.OpeningDays(ranges));
        return dto;
    }
}

