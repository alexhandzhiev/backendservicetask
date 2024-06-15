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

    private PlaceInfoDTO processPlaceInfo(PlaceInfo placeInfo) {
        ArrayList<String> days = new ArrayList<>(placeInfo.getOpeningDays().getDays().keySet());
        String currentDay = placeInfo.getOpeningDays().getDays().firstEntry().getKey();
        String lastDay = placeInfo.getOpeningDays().getDays().lastEntry().getKey();
        LinkedHashMap<String, List<PlaceInfo.OpeningDays.OpeningTime>> ranges = new LinkedHashMap<>();
        LinkedHashMap<String, List<PlaceInfo.OpeningDays.OpeningTime>> openingDays = placeInfo.getOpeningDays().getDays();
        for(int i = 0; i < days.size(); i++) {
            if(openingDays.get(currentDay) != null && !openingDays.get(currentDay).equals(openingDays.get(DAYS_ORDER.get(i+1)))) {
                if(lastDay != days.get(i)) {
                    ranges.put(currentDay + " - " + days.get(i), openingDays.get(currentDay));
                } else {
                    ranges.put(currentDay + " - " + lastDay, openingDays.get(lastDay));
                }
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

