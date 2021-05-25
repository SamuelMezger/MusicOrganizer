package model.youtube;

import java.util.Optional;

public class FullVideoInfo {
    private final String videoId;
    private final String videoTitle;
    private final String videoThumbnailURL;
    private final String videoDescription;

    private final Optional<String> title;
    private final Optional<String> artist;
    private final Optional<String> album;
    private final Optional<String> releaseDate;

    public FullVideoInfo(
            String videoId, String videoTitle, String videoThumbnailURL, String videoDescription,
            Optional<String> title, Optional<String> artist, Optional<String> album, Optional<String> releaseDate
    ) {
//        TODO Optional.ofNullable()
//        TODO dict
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.videoThumbnailURL = videoThumbnailURL;
        this.videoDescription = videoDescription;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.releaseDate = releaseDate;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public String getVideoThumbnailURL() {
        return videoThumbnailURL;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getArtist() {
        return artist;
    }

    public Optional<String> getAlbum() {
        return album;
    }

    public Optional<String> getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return "FullVideoInfo{" +
                "videoId='" + videoId + '\'' +
                ", videoTitle='" + videoTitle + '\'' +
                ", videoThumbnailURL='" + videoThumbnailURL + '\'' +
                ", videoDescription='" + videoDescription + '\'' +
                ", title=" + (title.orElse("---")) +
                ", artist=" + (artist.orElse("---")) +
                ", album=" + (album.orElse("---")) +
                ", releaseDate=" + (releaseDate.orElse("---")) +
                '}';
    }
}
