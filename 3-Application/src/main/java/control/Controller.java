package control;

import somepackage.*;
import view.MainView;
import view.TrackEditorView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionException;

public class Controller {
    private final YoutubeExtractor youtubeExtractor;
    private final MainView view;
    private final List<TrackEditorController> trackController;
    private final DownloadManager dlManager;
    private final BackgroundTaskFactory backgroundTaskFactory;

    public Controller(MainView view, DownloadManager dlManager, BackgroundTaskFactory backgroundTaskFactory, YoutubeExtractor youtubeExtractor) {
        this.view = view;
        this.backgroundTaskFactory = backgroundTaskFactory;
        this.youtubeExtractor = youtubeExtractor;
        this.dlManager = dlManager;

//        Runnable downloadTask = this.backgroundTaskFactory.createTask(view::pleaseComplainAboutNotFxThread);
//        new Thread(downloadTask).start();

        this.view.addDownloadButtonListener(actionEvent -> Controller.this.startDownload());
        this.view.addGetPLButtonListener(actionEvent -> Controller.this.startPLDownload());
//        this.view.addDownloadButtonListener(actionEvent -> Controller.this.startPLDownload());

        this.trackController = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            BasicVideo basicVideo = new SapherBasicVideo("OJdG8wsU8cw", "Rule the world", this.youtubeExtractor);
            TrackEditorView trackView = this.view.addTrackEditor();
            this.trackController.add(new TrackEditorController(basicVideo, this.dlManager, this.youtubeExtractor, trackView));
        }
    }

    private void startPLDownload() {
        String playListId = "PLvy1DvHP0feqGoG474BC0lTTjMNjroK1R";
//        this.dlManager.submitTask().andWhenCompleteUpdateGuiWith()
        this.dlManager.getCompletableFutureDLM(() -> {
            try {
                return this.youtubeExtractor.getBasicVideoInfos(playListId);
            } catch (YoutubeException e) {
                throw new CompletionException(e);
            }
        }, (basicInfos, throwable) -> {
            for (BasicVideoInfo basicInfo : basicInfos) {
                TrackEditorView trackView = this.view.addTrackEditor();
                System.out.println(basicInfo.getVideoTitle());
                this.trackController.add(new TrackEditorController(basicInfo, this.dlManager, this.youtubeExtractor, trackView));
            }
        });
    }



    private void startDownload() {
        String destinationFolder = "/tmp/test/";
        String videoId = "OJdG8wsU8cw";

        Runnable downloadTask = this.backgroundTaskFactory.createTask(() -> {
//        Runnable downloadTask = () -> {
            try {
//                TODO controller instead of view as
                this.youtubeExtractor.downloadAudio(videoId, destinationFolder, this.view);
            } catch (YoutubeException e) {
                e.printStackTrace();
            }
        });
//        };
        new Thread(downloadTask).start();
    }
}
