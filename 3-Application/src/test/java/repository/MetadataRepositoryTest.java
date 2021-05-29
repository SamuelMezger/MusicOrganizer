package repository;

import model.metadata.Metadata;
import model.metadata.Metadatum;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class MetadataRepositoryTest {

    @Test
    public void testOrder() {
        MetadataRepository repo = new MetadataRepository();
        repo.addFallback(new Metadata(Arrays.asList(
                new Metadatum.Title("bad"),
                new Metadatum.Artist("bad"),
                new Metadatum.Album("bad")
        )));

        repo.add(new Metadata(Collections.singletonList(
                new Metadatum.Title("best")
        )));

        repo.add(new Metadata(Arrays.asList(
                new Metadatum.Title("good"),
                new Metadatum.Artist("good")
        )));

        assertEquals(repo.getProgrammsBest(), new Metadata(Arrays.asList(
                new Metadatum.Title("best"),
                new Metadatum.Artist("good"),
                new Metadatum.Album("bad")
        )));
    }

    @Test
    public void testUserChoice() {
        MetadataRepository repo = new MetadataRepository();
        repo.addFallback(new Metadata(Arrays.asList(
                new Metadatum.Title("bad"),
                new Metadatum.Artist("bad"),
                new Metadatum.Album("bad")
        )));

        repo.add(new Metadata(Arrays.asList(
                new Metadatum.Title("best"),
                new Metadatum.Artist("best"),
                new Metadatum.Album("best")
        )));

        repo.setUserOverridden(new Metadata(Arrays.asList(
                new Metadatum.Title("user"),
//              No artist
                new Metadatum.Album("user")
        )));


        assertEquals(repo.getFinalChoice(), new Metadata(Arrays.asList(
                new Metadatum.Title("user"),
//              No artist
                new Metadatum.Album("user")
        )));
    }


    @Test
    public void testEmpty() {
        MetadataRepository repo = new MetadataRepository();
        assertEquals(repo.getProgrammsBest(), new Metadata(Collections.emptyList()));
    }

}