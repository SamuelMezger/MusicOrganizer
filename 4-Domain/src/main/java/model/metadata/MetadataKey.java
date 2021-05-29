package model.metadata;

import java.awt.image.BufferedImage;

public enum MetadataKey {

    COVER(BufferedImage.class),
    TITLE(String.class),
    ARTIST(String.class),
    ALBUM(String.class),
    TRACK_NUMBER(Integer.class),
    RELEASE_YEAR(Integer.class),
    GENRE(String.class),
    ;

    private final Class clazz;

    MetadataKey(Class clazz) {
        this.clazz = clazz;
    }

    public Class getTClass(){
        return this.clazz;
    }
}
