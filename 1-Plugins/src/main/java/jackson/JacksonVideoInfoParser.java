package jackson;

import anotherpackage.FlatVideoInfoParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import sapher.JsonFlatVideoInfo;
import somepackage.FlatVideoInfo;

import java.io.IOException;
import java.util.Optional;

public class JacksonVideoInfoParser implements FlatVideoInfoParser {
    @Override
    public Optional<FlatVideoInfo> fromJson(String flatVideoInfoJson) {
        // Parse result
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return Optional.of(objectMapper.readValue(flatVideoInfoJson, JsonFlatVideoInfo.class));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
