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
    private final List<MetadataFinder> metadataFinders;

    public Controller(MainView view, TaskManager taskManager, YoutubeExtractor youtubeExtractor, List<MetadataFinder> metadataFinders) {
        this.view = view;
        this.youtubeExtractor = youtubeExtractor;
        this.taskManager = taskManager;
        this.metadataFinders = metadataFinders;

        this.view.addGetPLButtonListener(actionEvent -> {
            String url = view.getPlUrl();
            this.startPlSync(url);
        });

        this.trackControllers = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
//            BasicVideoInfo basicVideoInfo = new BasicVideoInfo("OJdG8wsU8cw", "Rule the world");
            BasicVideoInfo basicVideoInfo = new BasicVideoInfo("JQGRg8XBnB4", "MOMOLAND");
            TrackEditorView trackView = this.view.addTrackEditor();
            this.trackControllers.add(new TrackEditorController(
                    basicVideoInfo,
                    trackView,
                    this.taskManager,
                    this.youtubeExtractor,
                    this.metadataFinders
            ));
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
                        this.trackControllers.add(new TrackEditorController(
                                basicInfo,
                                trackView,
                                this.taskManager,
                                this.youtubeExtractor,
                                this.metadataFinders
                        ));
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
