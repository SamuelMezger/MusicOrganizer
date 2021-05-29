package model.metadata;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class Metadatum<T> {
    private final MetadataKey key;
    private final T value;

    private Metadatum(MetadataKey key, T value) {

        this.key = key;
        this.value = value;
    }

    public MetadataKey getKey() {
        return this.key;
    }

    public T getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.getKey() + ": " + this.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Metadatum<?> metadatum = (Metadatum<?>) o;
        return this.key == metadatum.key && Objects.equals(this.value, metadatum.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.value);
    }

    public static class Cover extends Metadatum<BufferedImage> {
        public Cover(BufferedImage value) {
            super(MetadataKey.COVER, value);
        }

//        TODO come up with some method to compare images to override equals & hashCode

        @Override
        public String toString() {
            return this.getKey() + ": " + this.getValue().getWidth() + "x" + this.getValue().getHeight();
        }
    }

    public static class Title extends Metadatum<String> {
        public Title(String value) {
            super(MetadataKey.TITLE, value);
        }
    }

    public static class Artist extends Metadatum<String> {
        public Artist(String value) {
            super(MetadataKey.ARTIST, value);
        }
    }

    public static class Album extends Metadatum<String> {
        public Album(String value) {
            super(MetadataKey.ALBUM, value);
        }
    }

    public static class TrackNumber extends Metadatum<Integer> {
        public TrackNumber(Integer value) {
            super(MetadataKey.TRACK_NUMBER, value);
        }
    }

    public static class ReleaseYear extends Metadatum<Integer> {
        public ReleaseYear(Integer value) {
            super(MetadataKey.RELEASE_YEAR, value);
        }
    }

    public static class Genre extends Metadatum<String> {
        public Genre(String value) {
            super(MetadataKey.GENRE, value);
        }
    }
}
