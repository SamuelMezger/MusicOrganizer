package repository;

import model.metadata.Metadata;
import model.metadata.MetadataField;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class MetadataSorterTest {

    @Test
    public void testOrder() {
        MetadataSorter repo = new MetadataSorter();
        repo.addFallback(new Metadata(Arrays.asList(
                new MetadataField.Title("bad"),
                new MetadataField.Artist("bad"),
                new MetadataField.Album("bad")
        )));

        repo.add(new Metadata(Collections.singletonList(
                new MetadataField.Title("best")
        )));

        repo.add(new Metadata(Arrays.asList(
                new MetadataField.Title("good"),
                new MetadataField.Artist("good")
        )));

        assertEquals(repo.getProgrammsBest(), new Metadata(Arrays.asList(
                new MetadataField.Title("best"),
                new MetadataField.Artist("good"),
                new MetadataField.Album("bad")
        )));
    }

    @Test
    public void testUserChoice() {
        MetadataSorter repo = new MetadataSorter();
        repo.addFallback(new Metadata(Arrays.asList(
                new MetadataField.Title("bad"),
                new MetadataField.Artist("bad"),
                new MetadataField.Album("bad")
        )));

        repo.add(new Metadata(Arrays.asList(
                new MetadataField.Title("best"),
                new MetadataField.Artist("best"),
                new MetadataField.Album("best")
        )));

        repo.setUserOverridden(new Metadata(Arrays.asList(
                new MetadataField.Title("user"),
//              No artist
                new MetadataField.Album("user")
        )));


        assertEquals(repo.getFinalChoice(), new Metadata(Arrays.asList(
                new MetadataField.Title("user"),
//              No artist
                new MetadataField.Album("user")
        )));
    }


    @Test
    public void testEmpty() {
        MetadataSorter repo = new MetadataSorter();
        assertEquals(repo.getProgrammsBest(), new Metadata(Collections.emptyList()));
    }

}