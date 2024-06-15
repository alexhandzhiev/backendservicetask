package task.backend.ch.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import task.backend.ch.model.PlaceInfoDTO;
import task.backend.ch.service.PlaceService;

@RestController
public class PlaceController {
    private static final Logger log = LoggerFactory.getLogger(PlaceController.class);

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/place/{placeId}")
    public Mono<ResponseEntity<PlaceInfoDTO>> getPlaceSummary(@PathVariable String placeId) {
        return placeService.getPlaceSummary(placeId)
                .map(placeInfoDTO -> {
                    log.info("Place info retrieved successfully for placeId: {}", placeId);
                    return ResponseEntity.ok(placeInfoDTO);
                })
                .onErrorResume(ex -> {
                    log.error("Something happened when searching for placeId: {}", placeId, ex);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
