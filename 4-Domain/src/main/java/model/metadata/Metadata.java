package model.metadata;


import model.metadata.MetadataField.*;

import java.util.*;

public class Metadata {
    private final EnumMap<MetadataKey, MetadataField> metadata;

    public Metadata(List<MetadataField> list) {
        this.metadata = new EnumMap<>(MetadataKey.class);
        list.forEach(field -> this.metadata.put(field.getKey(), field));
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

    public Map<MetadataKey, MetadataField> asMap() {
        return Collections.unmodifiableMap(this.metadata);
    }


    public Optional<MetadataField> get(MetadataKey key) {
        return Optional.ofNullable(this.metadata.get(key));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder().append("Metadata{");
        this.metadata.forEach((metadataKey, field) ->
                builder.append(field.toString()).append(", "));
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
