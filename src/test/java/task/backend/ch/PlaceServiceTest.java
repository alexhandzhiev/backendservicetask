package task.backend.ch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import task.backend.ch.model.PlaceInfo;
import task.backend.ch.model.PlaceInfoDTO;
import task.backend.ch.service.PlaceService;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class PlaceServiceTest {
//
//    @Mock
//    private WebClient.Builder webClientBuilder;
//
//    @Mock
//    private WebClient webClient;
//
//    @InjectMocks
//    private PlaceService placeService;
//
//    private static final String API_URL = "http://localhost:8080"; // Example URL
//
//    @BeforeEach
//    void setUp() {
//        // Initialize Mockito annotations
//        MockitoAnnotations.openMocks(this);
//
//        // Mock the WebClient.Builder to return the mock WebClient when build() is called
//        when(webClientBuilder.baseUrl(API_URL)).thenReturn(webClientBuilder);
//        when(webClientBuilder.build()).thenReturn(webClient);
//
//        // You can also directly set the API_URL for placeService if needed
//        placeService = new PlaceService(API_URL, webClientBuilder);
//    }
//
//    @Test
//    void testProcessPlaceInfo() {
//        // Create a sample PlaceInfo object
//        PlaceInfo placeInfo = new PlaceInfo();
//        placeInfo.setName("Sample Place");
//        placeInfo.setAddress("123 Test St");
//
//        // Create a sample OpeningDays object with test data
//        LinkedHashMap<String, List<PlaceInfo.OpeningDays.OpeningTime>> openingDaysMap = new LinkedHashMap<>();
//        openingDaysMap.put("monday", List.of(
//                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
//                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
//        openingDaysMap.put("tuesday", List.of(
//                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
//                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
//        openingDaysMap.put("wednesday", List.of(
//                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
//                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
//        openingDaysMap.put("thursday", List.of(
//                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
//                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
//        openingDaysMap.put("friday", List.of(
//                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
//                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
//        openingDaysMap.put("saturday", List.of(
//                new PlaceInfo.OpeningDays.OpeningTime("18:00", "00:00", "OPEN")));
//        openingDaysMap.put("sunday", List.of(
//                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN")));
//
//        // Set the OpeningDays in PlaceInfo
//        PlaceInfo.OpeningDays openingDays = new PlaceInfo.OpeningDays(openingDaysMap);
//        placeInfo.setOpeningDays(openingDays);
//
//        // Call the method to test
//        PlaceInfoDTO placeInfoDTO = placeService.processPlaceInfo(placeInfo);
//
//        // Verify the results
//        LinkedHashMap<String, List<PlaceInfo.OpeningDays.OpeningTime>> expectedRanges = new LinkedHashMap<>();
//        expectedRanges.put("monday - friday", List.of(
//                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
//                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
//        expectedRanges.put("saturday - saturday", List.of(
//                new PlaceInfo.OpeningDays.OpeningTime("18:00", "00:00", "OPEN")));
//        expectedRanges.put("sunday - sunday", List.of(
//                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN")));
//
//        assertEquals("Sample Place", placeInfoDTO.getName());
//        assertEquals("123 Test St", placeInfoDTO.getAddress());
//        assertEquals(expectedRanges, placeInfoDTO.getOpeningDays().getDays());
//    }
}
