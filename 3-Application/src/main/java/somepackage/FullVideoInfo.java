package somepackage;

import java.util.Optional;

public interface FullVideoInfo {
    String getVideoId();

    String getVideoTitle();

    String getVideoThumbnailURL();

    String getVideoDescription();

    Optional<String> getTitle();

    Optional<String> getArtist();

    Optional<String> getAlbum();

    Optional<String> getReleaseDate();
}
