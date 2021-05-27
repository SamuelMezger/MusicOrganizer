package model;

import java.awt.image.BufferedImage;
import java.util.Optional;

public class Metadata {

    private final Optional<BufferedImage> cover;
    private final Optional<String> title;
    private final Optional<String> artist;
    private final Optional<String> album;
    private final Optional<Integer> trackNumber;
    private final Optional<Integer> releaseYear;
    private final Optional<String> genre;

    public Metadata(
            Optional<BufferedImage> cover,
            Optional<String> title,
            Optional<String> artist,
            Optional<String> album,
            Optional<Integer> trackNumber,
            Optional<Integer> releaseYear,
            Optional<String> genre) {
//        TODO Optional.ofNullable()
//        TODO dict
        this.cover = cover;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.trackNumber = trackNumber;
        this.releaseYear = releaseYear;
        this.genre = genre;
    }

    public Optional<BufferedImage> getCover() {
        return this.cover;
    }

    public Optional<String> getTitle() {
        return this.title;
    }

    public Optional<String> getArtist() {
        return this.artist;
    }

    public Optional<String> getAlbum() {
        return this.album;
    }

    public Optional<Integer> getTrackNumber() {
        return this.trackNumber;
    }

    public Optional<Integer> getReleaseYear() {
        return this.releaseYear;
    }

    public Optional<String> getGenre() {
        return this.genre;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder().append("Metadata{");
        this.title.ifPresent(title -> builder.append("title:\t").append(title).append(", "));
        this.artist.ifPresent(artist -> builder.append("artist:\t").append(artist).append(", "));
        this.album.ifPresent(album -> builder.append("album:\t").append(album).append(", "));
        this.trackNumber.ifPresent(trackNumber -> builder.append("trackNumber:\t").append(trackNumber).append(", "));
        this.releaseYear.ifPresent(releaseYear -> builder.append("releaseYear:\t").append(releaseYear).append(", "));
        this.genre.ifPresent(genre -> builder.append("genre:\t").append(genre).append(", "));
        return builder.append("}").toString();
    }
}
