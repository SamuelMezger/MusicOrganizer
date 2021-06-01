package control;

import extraction.ExtractionException;
import extraction.MetadataFinder;
import extraction.YoutubeExtractor;
import model.youtube.BasicVideoInfo;
import use_cases.SyncPair;
import view.MainView;
import view.TrackEditorView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionException;

public class Controller {
    private final MainView view;
    private final TaskManager taskManager;
    private final YoutubeExtractor youtubeExtractor;
    private final List<MetadataFinder> metadataFinders;
    private final List<TrackEditorController> trackControllers = new ArrayList<>();;
    private final List<SyncPair> syncPairs = new ArrayList<>();

    public Controller(MainView view, TaskManager taskManager, YoutubeExtractor youtubeExtractor, List<MetadataFinder> metadataFinders) {
        this.view = view;
        this.youtubeExtractor = youtubeExtractor;
        this.taskManager = taskManager;
        this.metadataFinders = metadataFinders;

        this.view.addGetPLButtonListener(() -> {
            String url = view.getPlUrl();
            this.startPlSync(url);
        });

        for (int i = 0; i < 1; i++) {
            BasicVideoInfo basicVideoInfo = new BasicVideoInfo("OJdG8wsU8cw", "Rule the world");
//            BasicVideoInfo basicVideoInfo = new BasicVideoInfo("JQGRg8XBnB4", "MOMOLAND");
            TrackEditorView trackView = this.view.addTrackEditor();
            SyncPair syncPair = new SyncPair(basicVideoInfo, this.youtubeExtractor, this.metadataFinders);
            this.trackControllers.add(new TrackEditorController(
                    syncPair,
                    trackView,
                    this.taskManager
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
                .ifItFailsHandle(throwable -> {
                    this.view.enableDownloadButton();
                    this.view.showCouldNotGetPlaylistException(
                            "Could not get Playlist",
                            "Please make sure youtube-dl is installed and in your PATH, you have an internet connection, and that the playlist your trying to download is not private.",
                            throwable.getMessage());
                })
                .whenCompletedSuccessful(basicVideoInfos -> {
                    for (BasicVideoInfo basicInfo : basicVideoInfos) {
                        TrackEditorView trackView = this.view.addTrackEditor();
                        SyncPair syncPair = new SyncPair(basicInfo, this.youtubeExtractor, this.metadataFinders);
                        this.syncPairs.add(syncPair);
                        this.trackControllers.add(new TrackEditorController(
                                syncPair,
                                trackView,
                                this.taskManager
                        ));
                    }
                })
                .submit();
    }
}
