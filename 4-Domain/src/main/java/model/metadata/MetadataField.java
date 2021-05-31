package model.metadata;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class MetadataField<T> {
    private final MetadataKey key;
    private final T value;

    private MetadataField(MetadataKey key, T value) {

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
        MetadataField<?> metadataField = (MetadataField<?>) o;
        return this.key == metadataField.key && Objects.equals(this.value, metadataField.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.value);
    }



    public static final class Cover extends MetadataField<BufferedImage> {
        public Cover(BufferedImage value) {
            super(MetadataKey.COVER, value);
        }

//        TODO come up with some method to compare images to override equals & hashCode

        @Override
        public String toString() {
            return this.getKey() + ": " + this.getValue().getWidth() + "x" + this.getValue().getHeight();
        }
    }

    public static final class Title extends MetadataField<String> {
        public Title(String value) {
            super(MetadataKey.TITLE, value);
        }
    }

    public static final class Artist extends MetadataField<String> {
        public Artist(String value) {
            super(MetadataKey.ARTIST, value);
        }
    }

    public static final class Album extends MetadataField<String> {
        public Album(String value) {
            super(MetadataKey.ALBUM, value);
        }
    }

    public static final class TrackNumber extends MetadataField<Integer> {
        public TrackNumber(Integer value) {
            super(MetadataKey.TRACK_NUMBER, value);
        }
    }

    public static final class ReleaseYear extends MetadataField<Integer> {
        public ReleaseYear(Integer value) {
            super(MetadataKey.RELEASE_YEAR, value);
        }
    }

    public static final class Genre extends MetadataField<String> {
        public Genre(String value) {
            super(MetadataKey.GENRE, value);
        }
    }
}
