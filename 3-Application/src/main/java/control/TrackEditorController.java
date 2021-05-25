package control;

import model.youtube.BasicVideoInfo;
import model.youtube.FullVideoInfo;
import somepackage.*;
import view.TrackEditorView;

import java.util.concurrent.CompletionException;

public class TrackEditorController implements MyDownloadProgressCallback {
    private final TrackEditorView trackView;
    private final TaskManager taskManager;
    private final YoutubeExtractor youtubeExtractor;
    private final BasicVideoInfo basicVideo;

    public TrackEditorController(BasicVideoInfo basicVideo,
                                 TrackEditorView trackView,
                                 TaskManager taskManager,
                                 YoutubeExtractor youtubeExtractor
    ) {
        this.basicVideo = basicVideo;
        this.taskManager = taskManager;
        this.trackView = trackView;
        this.youtubeExtractor = youtubeExtractor;
        this.trackView.addDownloadButtonListener(actionEvent -> this.startAudioDownload());

        trackView.setVideoTitle(
                this.basicVideo.getVideoTitle()
        );
        this.startFullInfoDownload();
    }

    private void startFullInfoDownload() {
        this.taskManager
                .doInBackground(() -> {
                    try {
                        return this.youtubeExtractor.getFullVideoInfo(this.basicVideo.getVideoId());
                    } catch (YoutubeException e) {
                        throw new CompletionException(e);
                    }
                })
                .whenCompletedSuccessful(this::updateTrackView)
                .ifItFailsHandle(
                        (throwable) -> this.trackView.showVideoNotAvailable("new " + throwable.getMessage())
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
                            youtubeExtractor.downloadAudio(basicVideo.getVideoId(), "/tmp/test/", this);
                            return null;
                        } catch (YoutubeException e) {
                            throw new CompletionException(e);
                        }
                    }
                })
                .ifItFailsHandle(throwable -> {
                    this.trackView.enableDownloadButton();
                    this.trackView.showVideoNotAvailable(throwable.getMessage());
                })
                .submit();
    }

    private void updateTrackView(FullVideoInfo fullVideoInfo) {
        fullVideoInfo.getTitle().ifPresent(this.trackView::setTitle);
        fullVideoInfo.getAlbum().ifPresent(this.trackView::setAlbum);
        fullVideoInfo.getArtist().ifPresent(this.trackView::setArtist);
        fullVideoInfo.getReleaseDate().ifPresent(this.trackView::setReleaseDate);
        System.out.println(fullVideoInfo.getVideoThumbnailURL());
//        this.trackView.setAlbumCover();
    }


    @Override
    public void onProgressUpdate(float progress, long etaInSeconds) {
        this.trackView.onProgressUpdate(progress, etaInSeconds);
    }
}
