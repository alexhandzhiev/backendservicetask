package task.backend.ch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import task.backend.ch.model.PlaceInfo;
import task.backend.ch.model.PlaceInfoDTO;
import task.backend.ch.service.PlaceService;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class PlaceServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder; // Mock the WebClient.Builder

    @Mock
    private WebClient webClient; // Mock the WebClient

    @Autowired //@InjectMocks didnt w
    PlaceService placeService; // Inject the mocks into the PlaceService

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Configure the mock webClientBuilder to return the mock webClient
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
    }

    @Test
    void testProcessPlaceInfo_ClassicCase() {
        // Create a sample PlaceInfo object
        PlaceInfo placeInfo = new PlaceInfo();
        placeInfo.setName("Sample Place");
        placeInfo.setAddress("123 Test St");

        // Create a sample OpeningDays object with test data
        LinkedHashMap<String, List<PlaceInfo.OpeningDays.OpeningTime>> openingDaysMap = new LinkedHashMap<>();
        openingDaysMap.put("monday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
        openingDaysMap.put("tuesday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
        openingDaysMap.put("wednesday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
        openingDaysMap.put("thursday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
        openingDaysMap.put("friday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
        openingDaysMap.put("saturday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("18:00", "00:00", "OPEN")));
        openingDaysMap.put("sunday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("18:00", "00:00", "OPEN")));

        // Set the OpeningDays in PlaceInfo
        PlaceInfo.OpeningDays openingDays = new PlaceInfo.OpeningDays(openingDaysMap);
        placeInfo.setOpeningDays(openingDays);

        // Call the method to test
        PlaceInfoDTO placeInfoDTO = placeService.processPlaceInfo(placeInfo);

        // Verify the results
        LinkedHashMap<String, List<PlaceInfo.OpeningDays.OpeningTime>> expectedRanges = new LinkedHashMap<>();
        expectedRanges.put("monday - friday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
        expectedRanges.put("saturday - sunday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("18:00", "00:00", "OPEN")));

        assertEquals("Sample Place", placeInfoDTO.getName());
        assertEquals("123 Test St", placeInfoDTO.getAddress());
        assertEquals(expectedRanges, placeInfoDTO.getOpeningDays().getDays());
    }

    @Test
    void testProcessPlaceInfo_ShortWeekCase() {
        // Create a sample PlaceInfo object
        PlaceInfo placeInfo = new PlaceInfo();
        placeInfo.setName("Sample Place");
        placeInfo.setAddress("123 Test St");

        // Create a sample OpeningDays object with test data
        LinkedHashMap<String, List<PlaceInfo.OpeningDays.OpeningTime>> openingDaysMap = new LinkedHashMap<>();

        openingDaysMap.put("tuesday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
        openingDaysMap.put("wednesday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
        openingDaysMap.put("thursday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
        openingDaysMap.put("friday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));

        // Set the OpeningDays in PlaceInfo
        PlaceInfo.OpeningDays openingDays = new PlaceInfo.OpeningDays(openingDaysMap);
        placeInfo.setOpeningDays(openingDays);

        // Call the method to test
        PlaceInfoDTO placeInfoDTO = placeService.processPlaceInfo(placeInfo);

        // Verify the results
        LinkedHashMap<String, List<PlaceInfo.OpeningDays.OpeningTime>> expectedRanges = new LinkedHashMap<>();
        expectedRanges.put("tuesday - friday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));

        assertEquals("Sample Place", placeInfoDTO.getName());
        assertEquals("123 Test St", placeInfoDTO.getAddress());
        assertEquals(expectedRanges, placeInfoDTO.getOpeningDays().getDays());
    }

    @Test
    void testProcessPlaceInfo_ShortWeekCaseAndWeekend() {
        // Create a sample PlaceInfo object
        PlaceInfo placeInfo = new PlaceInfo();
        placeInfo.setName("Sample Place");
        placeInfo.setAddress("123 Test St");

        // Create a sample OpeningDays object with test data
        LinkedHashMap<String, List<PlaceInfo.OpeningDays.OpeningTime>> openingDaysMap = new LinkedHashMap<>();

        openingDaysMap.put("tuesday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
        openingDaysMap.put("wednesday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
        openingDaysMap.put("thursday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
        openingDaysMap.put("friday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
        openingDaysMap.put("saturday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("18:00", "00:00", "OPEN")));
        openingDaysMap.put("sunday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("18:00", "22:00", "OPEN")));

        // Set the OpeningDays in PlaceInfo
        PlaceInfo.OpeningDays openingDays = new PlaceInfo.OpeningDays(openingDaysMap);
        placeInfo.setOpeningDays(openingDays);

        // Call the method to test
        PlaceInfoDTO placeInfoDTO = placeService.processPlaceInfo(placeInfo);

        // Verify the results
        LinkedHashMap<String, List<PlaceInfo.OpeningDays.OpeningTime>> expectedRanges = new LinkedHashMap<>();
        expectedRanges.put("tuesday - friday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:30", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:30", "22:00", "OPEN")));
        expectedRanges.put("saturday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("18:00", "00:00", "OPEN")));
        expectedRanges.put("sunday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("18:00", "22:00", "OPEN")));

        assertEquals("Sample Place", placeInfoDTO.getName());
        assertEquals("123 Test St", placeInfoDTO.getAddress());
        assertEquals(expectedRanges, placeInfoDTO.getOpeningDays().getDays());
    }

    @Test
    void testProcessPlaceInfo_ShortWeekUniqueDays() {
        // Create a sample PlaceInfo object
        PlaceInfo placeInfo = new PlaceInfo();
        placeInfo.setName("Sample Place");
        placeInfo.setAddress("123 Test St");

        // Create a sample OpeningDays object with test data
        LinkedHashMap<String, List<PlaceInfo.OpeningDays.OpeningTime>> openingDaysMap = new LinkedHashMap<>();

        openingDaysMap.put("tuesday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:31", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:31", "22:00", "OPEN")));
        openingDaysMap.put("wednesday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:32", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:32", "22:00", "OPEN")));
        openingDaysMap.put("thursday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:33", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:33", "22:00", "OPEN")));

        // Set the OpeningDays in PlaceInfo
        PlaceInfo.OpeningDays openingDays = new PlaceInfo.OpeningDays(openingDaysMap);
        placeInfo.setOpeningDays(openingDays);

        // Call the method to test
        PlaceInfoDTO placeInfoDTO = placeService.processPlaceInfo(placeInfo);

        // Verify the results
        LinkedHashMap<String, List<PlaceInfo.OpeningDays.OpeningTime>> expectedRanges = new LinkedHashMap<>();
        expectedRanges.put("tuesday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:31", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:31", "22:00", "OPEN")));
        expectedRanges.put("wednesday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:32", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:32", "22:00", "OPEN")));
        expectedRanges.put("thursday", List.of(
                new PlaceInfo.OpeningDays.OpeningTime("11:33", "15:00", "OPEN"),
                new PlaceInfo.OpeningDays.OpeningTime("18:33", "22:00", "OPEN")));

        assertEquals("Sample Place", placeInfoDTO.getName());
        assertEquals("123 Test St", placeInfoDTO.getAddress());
        assertEquals(expectedRanges, placeInfoDTO.getOpeningDays().getDays());
    }
}
