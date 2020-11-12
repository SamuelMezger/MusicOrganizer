package jackson;

import anotherpackage.BasicVideoInfoParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import sapher.JsonBasicVideoInfo;
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
}
