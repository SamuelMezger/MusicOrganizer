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
    private final List<TrackEditorController> trackControllers;
    private final TaskManager taskManager;
    private final TaskFactory taskFactory;

    public Controller(MainView view, TaskManager taskManager, TaskFactory taskFactory, YoutubeExtractor youtubeExtractor) {
        this.view = view;
        this.taskFactory = taskFactory;
        this.youtubeExtractor = youtubeExtractor;
        this.taskManager = taskManager;


        this.view.addDownloadButtonListener(actionEvent -> this.startDownload());
        this.view.addGetPLButtonListener(actionEvent -> this.startPLDownload());

        this.trackControllers = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            BasicVideo basicVideo = new SapherBasicVideo("OJdG8wsU8cw", "Rule the world", this.youtubeExtractor);
            TrackEditorView trackView = this.view.addTrackEditor();
            this.trackControllers.add(new TrackEditorController(basicVideo, trackView, this.taskManager, this.youtubeExtractor));
        }
    }

    private void startPLDownload() {
        String playListId = "PLvy1DvHP0feqGoG474BC0lTTjMNjroK1R"; // test_available
//        String playListId = "PLvy1DvHP0feqX7YavO62Ff7D1omilCqVl"; // test

        this.view.disableDownloadButton();
        this.taskManager
                .doInBackground(() -> {
                    try {
                        return this.youtubeExtractor.getBasicVideoInfos(playListId);
                    } catch (YoutubeException e) {
                        throw new CompletionException(e);
                    }
                })
                .whenCompletedSuccessful(basicVideoInfos -> {
                    for (BasicVideoInfo basicInfo : basicVideoInfos) {
                        TrackEditorView trackView = this.view.addTrackEditor();
                        this.trackControllers.add(new TrackEditorController(basicInfo, trackView, this.taskManager, this.youtubeExtractor));
                    }
                })
                .ifItFailsHandle(throwable -> {
                    this.view.enableDownloadButton();
                    this.view.showCouldNotGetPlaylistException(
                            "Could not get Playlist",
                            "Please make sure you have an internet connection, and that the playlist your trying to download is not private.",
                            throwable.getMessage());
                })
                .submit();
    }


    private void startDownload() {
        String destinationFolder = "/tmp/test/";
        String videoId = "OJdG8wsU8cw";

        Runnable downloadTask = this.taskFactory.createTask(() -> {
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
