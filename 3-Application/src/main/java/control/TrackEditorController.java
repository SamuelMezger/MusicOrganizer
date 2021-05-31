package control;

import model.metadata.Metadata;
import model.metadata.MetadataField;
import model.metadata.MetadataKey;
import model.youtube.BasicVideoInfo;
import extraction.*;
import repository.MetadataSorter;
import view.TrackEditorView;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletionException;

public class TrackEditorController implements MyDownloadProgressCallback {
    private final TrackEditorView trackView;
    private final TaskManager taskManager;
    private final YoutubeExtractor youtubeExtractor;
    private final BasicVideoInfo basicVideo;
    private final List<MetadataFinder> metadataFinders;
    private final MetadataSorter metadataSorter;

    public TrackEditorController(BasicVideoInfo basicVideo,
                                 TrackEditorView trackView,
                                 TaskManager taskManager,
                                 YoutubeExtractor youtubeExtractor,
                                 List<MetadataFinder> metadataFinders
    ) {
        this.basicVideo = basicVideo;
        this.taskManager = taskManager;
        this.trackView = trackView;
        this.youtubeExtractor = youtubeExtractor;
        this.metadataFinders = metadataFinders;
        this.trackView.setDownloadButtonListener(this::startAudioDownload);
        this.trackView.setEditorValuesChangedListener(this::getValuesFromEditor);
        this.metadataSorter = new MetadataSorter();

        trackView.setVideoTitle(
                this.basicVideo.getVideoTitle()
        );
        this.startFullInfoDownload();
//        this.startMetadataSearch();
    }

    private void startFullInfoDownload() {
        this.taskManager
                .doInBackground(() -> {
                    try {
                        return this.youtubeExtractor.getFullVideoInfo(this.basicVideo.getVideoId());
                    } catch (ExtractionException | IOException e) {
                        throw new CompletionException(e);
                    }
                })
                .ifItFailsHandle( throwable -> {
                            throwable.printStackTrace();
                            this.trackView.showVideoNotAvailable("new " + throwable.getMessage());
                        }
                )
                .whenCompletedSuccessful(metadata -> {
                    this.metadataSorter.addFallback(metadata);
                    this.updateTrackView(metadata);
                    this.startMetadataSearch();
                })
                .submit();
    }

    public void startAudioDownload() {
        this.trackView.disableDownloadButton();
        this.taskManager
                .doInBackground(this.taskManager.new SupplierWithProgress<Void>(this) {
                    @Override
                    public Void get() {
                        try {
//                            TODO pass current working directory as absolute path to YoutubeExtractor constructor
                            TrackEditorController.this.youtubeExtractor.downloadAudio(
                                    TrackEditorController.this.basicVideo.getVideoId(),
                                    "/tmp/test/",
                                    this);
                            return null;
                        } catch (ExtractionException e) {
                            throw new CompletionException(e);
                        }
                    }
                })
                .ifItFailsHandle(throwable -> {
                    throwable.printStackTrace();
                    this.trackView.enableDownloadButton();
                    this.trackView.showVideoNotAvailable(throwable.getMessage());
                })
                .whenCompletedSuccessful(unused -> {
//
                })
                .submit();
    }

    private void startMetadataSearch() {
        SearchQueryBuilder searchQueryBuilder = new SearchQueryBuilder();

        HashSet<MetadataKey> fieldsUsedForSearch = new HashSet<>(Arrays.asList(
                MetadataKey.TITLE,
                MetadataKey.ARTIST
        ));
        Metadata currentBestMetadata = this.metadataSorter.getProgrammsBest();
        currentBestMetadata.asMap().entrySet().stream()
                .filter(keyFieldEntry -> fieldsUsedForSearch.contains(keyFieldEntry.getKey()))
                .map(entry -> entry.getValue().getValue())
                .forEach(value -> searchQueryBuilder.addSearchTerm((String) value));

        String searchTerm = searchQueryBuilder.toString();
        System.out.println(searchTerm);

        for (MetadataFinder finder : this.metadataFinders) {
            this.taskManager
                    .doInBackground(() -> {
                        try {
                            return finder.searchFor(searchTerm);
                        } catch (IOException | ExtractionException e) {
                            throw new CompletionException(e);
                        }
                    })
                    .ifItFailsHandle(throwable -> throwable.printStackTrace())
                    .whenCompletedSuccessful(t -> {
                        for (Metadata metadata : t) {
                            this.metadataSorter.add(metadata);
                        }
                        this.updateTrackView(this.metadataSorter.getProgrammsBest());
                    })
                    .submit();
        }
    }

    private void getValuesFromEditor() {
        List<MetadataField> info = new ArrayList<>();
        this.trackView.getAlbumCover().ifPresent(image -> info.add(new MetadataField.Cover(image)));
        if (!this.trackView.getTitle().isEmpty()) info.add(new MetadataField.Title(this.trackView.getTitle()));
        if (!this.trackView.getArtist().isEmpty()) info.add(new MetadataField.Artist(this.trackView.getArtist()));
        if (!this.trackView.getAlbum().isEmpty()) info.add(new MetadataField.Album(this.trackView.getAlbum()));
        if (!this.trackView.getTrackNumber().isEmpty()) info.add(new MetadataField.TrackNumber(Integer.parseInt(this.trackView.getTrackNumber())));
        if (!this.trackView.getReleaseYear().isEmpty()) info.add(new MetadataField.ReleaseYear(Integer.parseInt(this.trackView.getReleaseYear())));
        if (!this.trackView.getGenre().isEmpty()) info.add(new MetadataField.Genre(this.trackView.getGenre()));
        this.metadataSorter.setUserOverriddenButTestIfItsReallyFromUser(new Metadata(info));
    }


    private void updateTrackView(Metadata metadata) {
        metadata.getCover().ifPresent(cover -> this.trackView.setAlbumCover(cover.getValue()));
        metadata.getTitle().ifPresent(title -> this.trackView.setTitle(title.getValue()));
        metadata.getArtist().ifPresent(artist -> this.trackView.setArtist(artist.getValue()));
        metadata.getAlbum().ifPresent(album -> this.trackView.setAlbum(album.getValue()));
        metadata.getTrackNumber().ifPresent(trackNumber -> this.trackView.setTrackNumber(trackNumber.getValue().toString()));
        metadata.getReleaseYear().ifPresent(releaseYear -> this.trackView.setReleaseYear(releaseYear.getValue().toString()));
        metadata.getGenre().ifPresent(genre -> this.trackView.setGenre(genre.getValue()));
    }


    @Override
    public void onProgressUpdate(float progress, long etaInSeconds) {
        this.trackView.onProgressUpdate(progress, etaInSeconds);
    }
}
