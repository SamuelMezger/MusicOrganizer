package control;

import extraction.*;
import model.metadata.Metadata;
import model.metadata.MetadataField;
import view.TrackEditorView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionException;

public class TrackEditorController implements MyDownloadProgressCallback {
    private final TrackEditorView trackView;
    private final TaskManager taskManager;
    private final TrackSyncer trackSyncer;

    public TrackEditorController(TrackSyncer trackSyncer,
                                 TrackEditorView trackView,
                                 TaskManager taskManager
    ) {
        this.trackSyncer = trackSyncer;
        this.taskManager = taskManager;
        this.trackView = trackView;
        this.trackView.setDownloadButtonListener(this::startAudioDownload);
        this.trackView.setEditorValuesChangedListener(this::onEditorValuesChanged);

        trackView.setVideoTitle(this.trackSyncer.getInitialInfo().getVideoTitle());
        this.startFullInfoDownloadAndMetadataSearch();
    }

    public void startAudioDownload() {
        this.trackView.disableDownloadButton();
        this.taskManager
                .doInBackground(this.taskManager.new SupplierWithProgress<File>(this) {
                    @Override
                    public File get() {
                        try {
                            TrackEditorController.this.trackSyncer.downloadAudio(this);
                            return null;
                        } catch (ExtractionException | FileNotFoundException e) {
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

    private void startFullInfoDownloadAndMetadataSearch() {
        this.taskManager
                .doInBackground(() -> {
                    try {
                        this.trackSyncer.downloadFullInfo();
                        return null;
                    } catch (ExtractionException | IOException e) {
                        throw new CompletionException(e);
                    }
                })
                .ifItFailsHandle( throwable -> {
                            throwable.printStackTrace();
                            this.trackView.showVideoNotAvailable("new " + throwable.getMessage());
                        }
                )
                .whenCompletedSuccessful(Null -> {
                    this.updateTrackView(this.trackSyncer.getCurrentChoice());
                    this.startMetadataSearch();
                })
                .submit();
    }

    private void startMetadataSearch() {
        this.taskManager
                .doInBackground(() -> {
                    try {
                        this.trackSyncer.searchMetadata();
                        return null;
                    } catch (IOException | ExtractionException e) {
                        throw new CompletionException(e);
                    }
                })
//                TODO propagate error message to view
                .ifItFailsHandle(throwable -> throwable.printStackTrace())
                .whenCompletedSuccessful(t -> {
                    this.updateTrackView(this.trackSyncer.getCurrentChoice());
//                    this.updateResultChooser(this.trackEditor.getAllFoundMetadata());
                })
                .submit();
    }

    private void onEditorValuesChanged() {
        Metadata newMetadata = this.getValuesFromEditor();
        this.trackSyncer.userChangedValuesTo(newMetadata);
    }


    private Metadata getValuesFromEditor() {
        List<MetadataField> info = new ArrayList<>();
        this.trackView.getAlbumCover().ifPresent(image -> info.add(new MetadataField.Cover(image)));
        if (!this.trackView.getTitle().isEmpty()) info.add(new MetadataField.Title(this.trackView.getTitle()));
        if (!this.trackView.getArtist().isEmpty()) info.add(new MetadataField.Artist(this.trackView.getArtist()));
        if (!this.trackView.getAlbum().isEmpty()) info.add(new MetadataField.Album(this.trackView.getAlbum()));
        if (!this.trackView.getTrackNumber().isEmpty()) info.add(new MetadataField.TrackNumber(Integer.parseInt(this.trackView.getTrackNumber())));
        if (!this.trackView.getReleaseYear().isEmpty()) info.add(new MetadataField.ReleaseYear(Integer.parseInt(this.trackView.getReleaseYear())));
        if (!this.trackView.getGenre().isEmpty()) info.add(new MetadataField.Genre(this.trackView.getGenre()));
        return new Metadata(info);
    }


    private void updateTrackView(Metadata metadata) {
        this.stopListeningForEditorValuesChanged();
        metadata.getCover().ifPresent(cover -> this.trackView.setAlbumCover(cover.getValue()));
        metadata.getTitle().ifPresent(title -> this.trackView.setTitle(title.getValue()));
        metadata.getArtist().ifPresent(artist -> this.trackView.setArtist(artist.getValue()));
        metadata.getAlbum().ifPresent(album -> this.trackView.setAlbum(album.getValue()));
        metadata.getTrackNumber().ifPresent(trackNumber -> this.trackView.setTrackNumber(trackNumber.getValue().toString()));
        metadata.getReleaseYear().ifPresent(releaseYear -> this.trackView.setReleaseYear(releaseYear.getValue().toString()));
        metadata.getGenre().ifPresent(genre -> this.trackView.setGenre(genre.getValue()));
        this.startListeningForEditorValuesChanged();
    }

    private void startListeningForEditorValuesChanged(){
        this.trackView.setEditorValuesChangedListener(this::onEditorValuesChanged);
    }
    private void stopListeningForEditorValuesChanged(){
        this.trackView.setEditorValuesChangedListener(() -> {});
    }

    @Override
    public void onProgressUpdate(float progress, long etaInSeconds) {
        this.trackView.onProgressUpdate(progress, etaInSeconds);
    }
}
