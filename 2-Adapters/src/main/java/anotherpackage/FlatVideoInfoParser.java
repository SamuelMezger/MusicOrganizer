package anotherpackage;

import somepackage.FlatVideoInfo;

import java.util.Optional;

public interface FlatVideoInfoParser {
    Optional<FlatVideoInfo> fromJson(String json);
}
