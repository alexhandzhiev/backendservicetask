package task.backend.ch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class PlaceInfo {

    @JsonProperty("displayed_what")
    private String name;

    @JsonProperty("displayed_where")
    private String address;

    @JsonProperty("opening_hours")
    private OpeningDays openingDays;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter @NoArgsConstructor @AllArgsConstructor
    public static class OpeningDays {

        @JsonProperty("days")
        private LinkedHashMap<String, List<OpeningTime>> days;

        @JsonIgnoreProperties(ignoreUnknown = true)
        @Getter
        public static class OpeningTime {
            @JsonProperty("start")
            private String start;
            @JsonProperty("end")
            private String end;
            @JsonProperty("type")
            private String type;

            // Default constructor
            public OpeningTime() {
                // You can initialize fields to default values if necessary
                this.start = "";
                this.end = "";
                this.type = "";
            }

            // Parameterized constructor
            public OpeningTime(String start, String end, String type) {
                this.start = start;
                this.end = end;
                this.type = type;
            }

            @Override
            public String toString() {
                return "OpeningTime{" +
                        "start='" + start + '\'' +
                        ", end='" + end + '\'' +
                        ", type='" + type + '\'' +
                        '}';
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                OpeningTime that = (OpeningTime) o;
                return Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(type, that.type);
            }

            @Override
            public int hashCode() {
                return Objects.hash(start, end, type);
            }
        }
    }
}