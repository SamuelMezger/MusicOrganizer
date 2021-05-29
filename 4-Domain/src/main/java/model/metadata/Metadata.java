package model.metadata;


import model.metadata.Metadatum.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Metadata {
    private final EnumMap<MetadataKey, Metadatum> metadata;

    public Metadata(List<Metadatum> list) {
        this.metadata = new EnumMap<>(MetadataKey.class);
        list.forEach(metadatum -> this.metadata.put(metadatum.getKey(), metadatum));
    }

    public Optional<Cover> getCover() {
        return Optional.ofNullable((Cover) this.metadata.get(MetadataKey.COVER));
    }

    public Optional<Title> getTitle() {
        return Optional.ofNullable((Title) this.metadata.get(MetadataKey.TITLE));
    }

    public Optional<Artist> getArtist() {
        return Optional.ofNullable((Artist) this.metadata.get(MetadataKey.ARTIST));
    }

    public Optional<Album> getAlbum() {
        return Optional.ofNullable((Album) this.metadata.get(MetadataKey.ALBUM));
    }

    public Optional<TrackNumber> getTrackNumber() {
        return Optional.ofNullable((TrackNumber) this.metadata.get(MetadataKey.TRACK_NUMBER));
    }

    public Optional<ReleaseYear> getReleaseYear() {
        return Optional.ofNullable((ReleaseYear) this.metadata.get(MetadataKey.RELEASE_YEAR));
    }

    public Optional<Genre> getGenre() {
        return Optional.ofNullable((Genre) this.metadata.get(MetadataKey.GENRE));
    }

    public EnumMap<MetadataKey, Metadatum> asMap() {
        return this.metadata;
    }

    public Optional<Metadatum> get(MetadataKey key) {
        return Optional.ofNullable(this.metadata.get(key));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder().append("Metadata{");
        this.metadata.forEach((metadataKey, metadatum) ->
                builder.append(metadatum.toString()).append(", "));
        return builder.append("}").toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Metadata metadata1 = (Metadata) o;
        return Objects.equals(this.metadata, metadata1.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.metadata);
    }
}
