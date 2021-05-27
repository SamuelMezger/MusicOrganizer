package control;

import model.youtube.BasicVideoInfo;
import extraction.*;
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

    public Controller(MainView view, TaskManager taskManager, YoutubeExtractor youtubeExtractor) {
        this.view = view;
        this.youtubeExtractor = youtubeExtractor;
        this.taskManager = taskManager;

        this.view.addGetPLButtonListener(actionEvent -> {
            String url = view.getPlUrl();
            this.startPlSync(url);
        });

        this.trackControllers = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            BasicVideoInfo basicVideoInfo = new BasicVideoInfo("OJdG8wsU8cw", "Rule the world");
            TrackEditorView trackView = this.view.addTrackEditor();
            this.trackControllers.add(new TrackEditorController(basicVideoInfo, trackView, this.taskManager, this.youtubeExtractor));
        }
    }

    private void startPlSync(String url) {

        this.view.disableDownloadButton();
        this.taskManager
                .doInBackground(() -> {
                    try {
                        return this.youtubeExtractor.getBasicVideoInfos(url);
                    } catch (ExtractionException e) {
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
}
