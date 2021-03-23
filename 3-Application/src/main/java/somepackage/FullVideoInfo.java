package somepackage;

import java.util.Optional;

public interface FullVideoInfo extends BasicVideoInfo{
    String getVideoId();

    String getVideoTitle();

    String getVideoThumbnailURL();

    String getVideoDescription();

    Optional<String> getTitle();

    Optional<String> getArtist();

    Optional<String> getAlbum();

    Optional<String> getReleaseDate();
}
