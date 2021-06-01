package jaudiotagger;

import model.metadata.Metadata;
import model.metadata.MetadataField;
import model.metadata.MetadataKey;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.StandardArtwork;
import use_cases.AudioFileTagger;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class JAudioFileTagger implements AudioFileTagger {

    @Override
    public void tagFile(File audioFile, Metadata metadata) throws IOException {
        try {
            AudioFile jAudioFile = AudioFileIO.read(audioFile);
            Tag tag = jAudioFile.getTag();

            if (metadata.getCover().isPresent()) {
                File tmpImageFile = File.createTempFile("tmp", ".jpg");
                ImageIO.write(
                        metadata.getCover().get().getValue(),
                        "jpg",
                        tmpImageFile
                );
                tag.setField(StandardArtwork.createArtworkFromFile(tmpImageFile));
//                tag.setField(StandardArtwork.createArtworkFromMetadataBlockDataPicture(new MetadataBlockDataPicture()));
            }

            if (metadata.getArtist().isPresent())
                tag.setField(JAudioTranslator.translateKey(metadata.getArtist().get().getKey()), metadata.getArtist().get().getValue());

            for (Map.Entry<MetadataKey, MetadataField> entry : metadata.asMap().entrySet()) {
                if (entry.getKey() != MetadataKey.COVER) {
                    tag.setField(JAudioTranslator.translateKey(entry.getKey()), entry.getValue().getValue().toString());
                }
            }

            jAudioFile.commit();
        } catch (CannotReadException | ReadOnlyFileException | CannotWriteException e) {
            throw new IOException(e);
        } catch (TagException | InvalidAudioFrameException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static class JAudioTranslator {

        static final EnumMap<MetadataKey, FieldKey> dictionary = new EnumMap<>(MetadataKey.class){{
            put(MetadataKey.COVER, FieldKey.COVER_ART);
            put(MetadataKey.ARTIST, FieldKey.ARTIST);
            put(MetadataKey.ALBUM, FieldKey.ALBUM);
            put(MetadataKey.TITLE, FieldKey.TITLE);
            put(MetadataKey.TRACK_NUMBER, FieldKey.DISC_NO);
            put(MetadataKey.RELEASE_YEAR, FieldKey.YEAR);
            put(MetadataKey.GENRE, FieldKey.GENRE);
        }};

        public static FieldKey translateKey(MetadataKey key) {
            return dictionary.get(key);
        }

        public static StandardArtwork translateDatatype(MetadataField.Cover cover) throws IOException {
                File tmpImageFile = File.createTempFile("tmp", ".jpg");
                ImageIO.write(
                        cover.getValue(),
                        "jpg",
                        tmpImageFile
                );
                return StandardArtwork.createArtworkFromFile(tmpImageFile);
//                StandardArtwork.createArtworkFromMetadataBlockDataPicture(new MetadataBlockDataPicture());
        }
    }
}
