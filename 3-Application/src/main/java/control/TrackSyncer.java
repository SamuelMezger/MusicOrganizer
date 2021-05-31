package control;

import extraction.*;
import model.metadata.Metadata;
import model.metadata.MetadataKey;
import model.youtube.BasicVideoInfo;
import repository.MetadataSorter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class TrackSyncer {
    private final YoutubeExtractor youtubeExtractor;
    private final BasicVideoInfo initialInfo;
    private final List<MetadataFinder> metadataFinders;
    private final MetadataSorter metadataSorter;
    private File audioFile;

    public TrackSyncer(BasicVideoInfo initialInfo,
                       YoutubeExtractor youtubeExtractor,
                       List<MetadataFinder> metadataFinders
    ) {
        this.initialInfo = initialInfo;
        this.youtubeExtractor = youtubeExtractor;
        this.metadataFinders = metadataFinders;
        this.metadataSorter = new MetadataSorter();
    }

    public BasicVideoInfo getInitialInfo() {
        return this.initialInfo;
    }

    public Metadata getCurrentChoice() {
        return this.metadataSorter.getCurrentChoice();
    }

    public List<Metadata> getAllFoundMetadata() {
        return this.metadataSorter.getAllFoundMetadata();
    }

    public void downloadAudio(MyDownloadProgressCallback dlCallback) throws ExtractionException, FileNotFoundException {
//        TODO pass current working directory as absolute path to YoutubeExtractor constructor
        this.audioFile = this.youtubeExtractor.downloadAudio(
                this.initialInfo.getVideoId(),
                "/tmp/test/",
                dlCallback
        );
    }

    public void downloadFullInfo() throws IOException, ExtractionException {
        Metadata metadata = this.youtubeExtractor.getFullVideoInfo(this.initialInfo.getVideoId());
        this.metadataSorter.addFallback(metadata);
    }

    public void userChangedValuesTo(Metadata newMetadata) {
        this.metadataSorter.setUserOverriddenButTestIfItsReallyFromUser(newMetadata);
    }

    public void searchMetadata() throws IOException, ExtractionException {
        SearchQueryBuilder searchQueryBuilder = new SearchQueryBuilder();

        HashSet<MetadataKey> fieldsUsedForSearch = new HashSet<>(Arrays.asList(
                MetadataKey.TITLE,
                MetadataKey.ARTIST
        ));
        Metadata currentBestMetadata = this.metadataSorter.getCurrentChoice();
        currentBestMetadata.asMap().entrySet().stream()
                .filter(keyFieldEntry -> fieldsUsedForSearch.contains(keyFieldEntry.getKey()))
                .map(entry -> entry.getValue().getValue())
                .forEach(value -> searchQueryBuilder.addSearchTerm((String) value));

        String searchTerm = searchQueryBuilder.toString();
        System.out.println(searchTerm);

        for (MetadataFinder finder : this.metadataFinders) {
            List<Metadata> results = finder.searchFor(searchTerm);
            for (Metadata metadata : results) {
                this.metadataSorter.add(metadata);
            }
        }
    }
}
