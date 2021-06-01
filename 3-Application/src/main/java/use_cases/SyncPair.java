package use_cases;

import extraction.*;
import model.metadata.Metadata;
import model.metadata.MetadataKey;
import model.youtube.BasicVideoInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class SyncPair {
    private final BasicVideoInfo initialInfo;
    private final AudioFileTagger audioTagger;
    private final YoutubeExtractor youtubeExtractor;
    private final List<MetadataFinder> metadataFinders;
    private final MetadataSorter metadataSorter;
    private Optional<File> audioFile = Optional.empty();

    public SyncPair(BasicVideoInfo initialInfo,
                    AudioFileTagger audioTagger,
                    YoutubeExtractor youtubeExtractor,
                    List<MetadataFinder> metadataFinders
    ) {
        this.initialInfo = initialInfo;
        this.audioTagger = audioTagger;
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

    public void downloadAudio(ProgressCallback dlCallback) throws ExtractionException, FileNotFoundException {
//        TODO pass current working directory as absolute path to YoutubeExtractor constructor
        this.audioFile = Optional.of(this.youtubeExtractor.downloadAudio(
                this.initialInfo.getVideoId(),
                "/tmp/test/",
                dlCallback
        ));
    }

    public void downloadFullInfo() throws IOException, ExtractionException {
        Metadata metadata = this.youtubeExtractor.getFullVideoInfo(this.initialInfo.getVideoId());
        this.metadataSorter.addFallback(metadata);
    }

    public void userChangedValuesTo(Metadata newMetadata) {
        this.metadataSorter.setUserOverriddenButTestIfItsReallyFromUser(newMetadata);
    }

    public void save() throws IOException {
        if (this.audioFile.isPresent())
                this.audioTagger.tagFile(this.audioFile.get(), this.metadataSorter.getCurrentChoice());
        else throw new IOException("No Audio File found");
    }

    public void searchMetadata() throws IOException, ExtractionException {
        String searchQuery = this.buildSearchQuery();
        for (MetadataFinder finder : this.metadataFinders) {
            List<Metadata> results = finder.searchFor(searchQuery);
            for (Metadata metadata : results) {
                this.metadataSorter.add(metadata);
            }
        }
    }

    private String buildSearchQuery() {
        HashSet<MetadataKey> fieldsUsedForSearch = new HashSet<>(Arrays.asList(
                MetadataKey.TITLE,
                MetadataKey.ARTIST
        ));

        SearchQueryBuilder searchQueryBuilder = new SearchQueryBuilder();

        this.metadataSorter.getCurrentChoice().asMap().entrySet().stream()
                .filter(keyFieldEntry -> fieldsUsedForSearch.contains(keyFieldEntry.getKey()))
                .map(entry -> entry.getValue().getValue())
                .forEach(value -> searchQueryBuilder.addSearchTerm((String) value));

        return searchQueryBuilder.toString();
    }
}
