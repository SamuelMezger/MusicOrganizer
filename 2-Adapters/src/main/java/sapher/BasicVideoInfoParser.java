package sapher;

import model.youtube.BasicVideoInfo;

import java.util.Optional;

public interface BasicVideoInfoParser {
    Optional<BasicVideoInfo> fromJson(String json);
}
