package task.backend.ch.model;

import lombok.*;

import java.util.LinkedHashMap;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class PlaceInfoDTO {
    private String name;
    private String address;
    private PlaceInfo.OpeningDays openingDays;

    public static class OpeningDays {
        private LinkedHashMap<String, List<task.backend.ch.model.PlaceInfo.OpeningDays.OpeningTime>> days;

        public OpeningDays(LinkedHashMap<String, List<task.backend.ch.model.PlaceInfo.OpeningDays.OpeningTime>> openingDays) {
            this.days = openingDays;
        }

        public static class OpeningTime {
            private String start;
            private String end;
            private String type;
        }
    }
}