package model.youtube;

import java.util.Optional;

public final class FullVideoInfo {
    private final String videoId;
    private final String videoTitle;
    private final String videoThumbnailURL;

    private final Optional<String> title;
    private final Optional<String> artist;
    private final Optional<String> album;
    private final Optional<String> releaseYear;

    public FullVideoInfo(
            String videoId, String videoTitle, String videoThumbnailURL,
            Optional<String> title, Optional<String> artist, Optional<String> album, Optional<String> releaseYear
    ) {
//        TODO Optional.ofNullable()
//        TODO dict
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.videoThumbnailURL = videoThumbnailURL;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.releaseYear = releaseYear;
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


    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getArtist() {
        return artist;
    }

    public Optional<String> getAlbum() {
        return album;
    }

    public Optional<String> getReleaseYear() {
        return releaseYear;
    }

    @Override
    public String toString() {
        return "FullVideoInfo{" +
                "videoId='" + videoId + '\'' +
                ", videoTitle='" + videoTitle + '\'' +
                ", videoThumbnailURL='" + videoThumbnailURL + '\'' +
                ", title=" + (title.orElse("---")) +
                ", artist=" + (artist.orElse("---")) +
                ", album=" + (album.orElse("---")) +
                ", releaseDate=" + (releaseYear.orElse("---")) +
                '}';
    }
}
