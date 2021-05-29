package control;

import model.metadata.Metadata;
import model.youtube.BasicVideoInfo;
import extraction.*;
import repository.MetadataRepository;
import view.TrackEditorView;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletionException;

public class TrackEditorController implements MyDownloadProgressCallback {
    private final TrackEditorView trackView;
    private final TaskManager taskManager;
    private final YoutubeExtractor youtubeExtractor;
    private final BasicVideoInfo basicVideo;
    private final List<MetadataFinder> metadataFinders;
    private final MetadataRepository metadataRepository;

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
        this.trackView.addDownloadButtonListener(actionEvent -> this.startAudioDownload());
        this.metadataRepository = new MetadataRepository();

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
                .whenCompletedSuccessful(metadata -> {
                    this.metadataRepository.addFallback(metadata);
                    this.updateTrackView(metadata);
                    this.startMetadataSearch();
                })
                .ifItFailsHandle( throwable -> {
                            throwable.printStackTrace();
                            this.trackView.showVideoNotAvailable("new " + throwable.getMessage());
                        }
                )
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
                .submit();
    }

    private void startMetadataSearch() {
        Metadata currentBestMetadata = this.metadataRepository.getProgrammsBest();
        SearchTermBuilder searchTermBuilder = new SearchTermBuilder();
        currentBestMetadata.getTitle().ifPresent(title -> searchTermBuilder.add(title.getValue()));
        currentBestMetadata.getArtist().ifPresent(artist -> searchTermBuilder.add(artist.getValue()));
        String searchTerm = searchTermBuilder.toString();
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
                    .whenCompletedSuccessful(t -> {
                        for (Metadata metadata : t) {
                            this.metadataRepository.add(metadata);
                        }
                        this.updateTrackView(this.metadataRepository.getProgrammsBest());
                    })
                    .ifItFailsHandle(throwable -> throwable.printStackTrace())
                    .submit();
        }
    }


    private void updateTrackView(Metadata metadata) {
        metadata.getCover().ifPresent(cover -> this.trackView.setAlbumCover(cover.getValue()));
        metadata.getTitle().ifPresent(title -> this.trackView.setTitle(title.getValue()));
        metadata.getArtist().ifPresent(artist -> this.trackView.setArtist(artist.getValue()));
        metadata.getAlbum().ifPresent(album -> this.trackView.setAlbum(album.getValue()));
        metadata.getTrackNumber().ifPresent(trackNumber -> this.trackView.setTrackNumber(trackNumber.getValue()));
        metadata.getReleaseYear().ifPresent(releaseYear -> this.trackView.setReleaseYear(releaseYear.getValue()));
        metadata.getGenre().ifPresent(genre -> this.trackView.setGenre(genre.getValue()));
    }


    @Override
    public void onProgressUpdate(float progress, long etaInSeconds) {
        this.trackView.onProgressUpdate(progress, etaInSeconds);
    }
}
