package sapher;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import somepackage.BasicVideoInfo;

import java.io.IOException;
import java.util.Optional;

public class JacksonVideoInfoParser implements BasicVideoInfoParser {
    @Override
    public Optional<BasicVideoInfo> fromJson(String flatVideoInfoJson) {
        // Parse result
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return Optional.of(objectMapper.readValue(flatVideoInfoJson, JsonBasicVideoInfo.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private static class JsonBasicVideoInfo implements BasicVideoInfo {

        private final String videoTitle;
        private final String videoId;

        @JsonAnySetter
        public void methodToCatchUnneededValuesFromJson(String propertyKey, Object value) {
            System.out.println(propertyKey + value);
        }

        @JsonCreator
        public JsonBasicVideoInfo(
                @JsonProperty(value = "id", required = true) String videoId,
                @JsonProperty(value = "title", required = true) String videoTitle) {
            this.videoId = videoId;
            this.videoTitle = videoTitle;
        }

        @Override
        public String getVideoId() {
            return videoId;
        }

        @Override
        public String getVideoTitle() {
            return videoTitle;
        }

        @Override
        public String toString() {
            return "\"" + videoId + "\", \"" + videoTitle + "\"";
        }
    }
    
}
