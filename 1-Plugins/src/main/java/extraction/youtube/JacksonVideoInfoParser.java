package extraction.youtube;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import extraction.ExtractionException;
import model.youtube.BasicVideoInfo;

import java.io.IOException;

public class JacksonVideoInfoParser implements BasicVideoInfoParser {
    @Override
    public BasicVideoInfo fromJson(String flatVideoInfoJson) throws ExtractionException {
        // Parse result
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonBasicVideoInfo jsonInfo = objectMapper.readValue(flatVideoInfoJson, JsonBasicVideoInfo.class);
            return new BasicVideoInfo(jsonInfo.getVideoId(), jsonInfo.getVideoTitle());
        } catch (IOException e) {
            throw new ExtractionException(e);
        }
    }

    private static class JsonBasicVideoInfo {

        private final String videoTitle;
        private final String videoId;

        @JsonAnySetter
        public void methodToCatchUnneededValuesFromJson(String propertyKey, Object value) {}

        @JsonCreator
        public JsonBasicVideoInfo(
                @JsonProperty(value = "id", required = true) String videoId,
                @JsonProperty(value = "title", required = true) String videoTitle) {
            this.videoId = videoId;
            this.videoTitle = videoTitle;
        }

        public String getVideoId() {
            return videoId;
        }

        public String getVideoTitle() {
            return videoTitle;
        }
    }
}
