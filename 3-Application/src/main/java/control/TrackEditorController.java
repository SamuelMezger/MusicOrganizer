package control;

import somepackage.*;
import view.TrackEditorView;

import java.util.concurrent.CompletionException;

public class TrackEditorController {
    private final TrackEditorView trackView;
    private final DownloadManager dlManager;

    public TrackEditorController(BasicVideoInfo basicVideo, DownloadManager dlManager, YoutubeExtractor youtubeExtractor, TrackEditorView trackView) {
        this.dlManager = dlManager;
        this.trackView = trackView;
        trackView.setVideoTitle(basicVideo.getVideoTitle());

        this.downloadFullVideoInfo(basicVideo, youtubeExtractor);
    }

    private void downloadFullVideoInfo(BasicVideoInfo basicVideo, YoutubeExtractor youtubeExtractor) {
        this.dlManager.getCompletableFutureDLM(() -> {
            try {
                return youtubeExtractor.getFullVideoInfo(basicVideo.getVideoId());
            } catch (YoutubeException e) {
                throw new CompletionException(e);
            }
        }, (fullVideoInfo, throwable) -> this.updateTrackView(fullVideoInfo));
    }

    private void updateTrackView(FullVideoInfo fullVideoInfo) {
        fullVideoInfo.getTitle().ifPresent(this.trackView::setTitle);
        fullVideoInfo.getAlbum().ifPresent(this.trackView::setAlbum);
        fullVideoInfo.getArtist().ifPresent(this.trackView::setArtist);
        fullVideoInfo.getReleaseDate().ifPresent(this.trackView::setReleaseDate);
    }
}
