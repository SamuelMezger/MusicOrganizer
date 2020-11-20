package sapher;

import somepackage.BasicVideoInfo;

import java.util.Optional;

public interface BasicVideoInfoParser {
    Optional<BasicVideoInfo> fromJson(String json);
}
