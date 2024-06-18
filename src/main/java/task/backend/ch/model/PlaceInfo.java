package task.backend.ch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @JsonIgnoreProperties(ignoreUnknown = true) @Getter @NoArgsConstructor
    public static class OpeningDays {

        @JsonProperty("days")
        private LinkedHashMap<String, List<OpeningTime>> days;

        public OpeningDays(LinkedHashMap<String, List<OpeningTime>> openingDays) {
            this.days = openingDays;
        }

        @JsonIgnoreProperties(ignoreUnknown = true) @Getter @AllArgsConstructor
        public static class OpeningTime {
            @JsonProperty("start")
            private String start;
            @JsonProperty("end")
            private String end;
            @JsonProperty("type")
            private String type;

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