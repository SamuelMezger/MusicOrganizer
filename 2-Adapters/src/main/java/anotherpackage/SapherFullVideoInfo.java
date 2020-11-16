package anotherpackage;

import somepackage.FullVideoInfo;

import java.util.Optional;

public class SapherFullVideoInfo implements FullVideoInfo {
    private final String videoId;
    private final String videoTitle;
    private final String videoThumbnailURL;
    private final String videoDescription;

    private final Optional<String> title;
    private final Optional<String> artist;
    private final Optional<String> album;
    private final Optional<String> releaseDate;

    public SapherFullVideoInfo(
            String videoId, String videoTitle, String videoThumbnailURL, String videoDescription,
            Optional<String> title, Optional<String> artist, Optional<String> album, Optional<String> releaseDate
    ) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.videoThumbnailURL = videoThumbnailURL;
        this.videoDescription = videoDescription;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.releaseDate = releaseDate;
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
    public String getVideoThumbnailURL() {
        return videoThumbnailURL;
    }

    @Override
    public String getVideoDescription() {
        return videoDescription;
    }

    @Override
    public Optional<String> getTitle() {
        return title;
    }

    @Override
    public Optional<String> getArtist() {
        return artist;
    }

    @Override
    public Optional<String> getAlbum() {
        return album;
    }

    @Override
    public Optional<String> getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return "SapherFullVideoInfo{" +
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
