package control;

import extraction.ExtractionException;
import extraction.MetadataFinder;
import extraction.YoutubeExtractor;
import model.youtube.BasicVideoInfo;
import use_cases.AudioFileTagger;
import use_cases.SyncPair;
import view.MainView;
import view.TrackEditorView;

import java.io.IOException;
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
    private final AudioFileTagger audioTagger;

    public Controller(MainView view, TaskManager taskManager, List<MetadataFinder> metadataFinders, AudioFileTagger audioTagger, YoutubeExtractor youtubeExtractor) {
        this.view = view;
        this.audioTagger = audioTagger;
        this.youtubeExtractor = youtubeExtractor;
        this.taskManager = taskManager;
        this.metadataFinders = metadataFinders;

        this.view.setGetPLButtonListener(() -> {
            String url = view.getPlUrl();
            this.startPlSync(url);
        });
        this.view.setSaveButtonListener(this::saveAll);

        for (int i = 0; i < 1; i++) {
            BasicVideoInfo basicVideoInfo = new BasicVideoInfo("OJdG8wsU8cw", "Rule the world");
//            BasicVideoInfo basicVideoInfo = new BasicVideoInfo("JQGRg8XBnB4", "MOMOLAND");
            TrackEditorView trackView = this.view.addTrackEditor();
            SyncPair syncPair = new SyncPair(
                    basicVideoInfo,
                    this.audioTagger,
                    this.youtubeExtractor,
                    this.metadataFinders
            );
            this.syncPairs.add(syncPair);
            this.trackControllers.add(new TrackEditorController(
                    syncPair,
                    trackView,
                    this.taskManager
            ));
        }
    }

    private void saveAll() {
        StringBuilder builder = new StringBuilder();
        for (SyncPair syncPair : this.syncPairs) {
            try {
                syncPair.save();
            } catch (IOException e) {
                builder.append("Video: ").append(syncPair.getInitialInfo().getVideoTitle()).append("\n")
                       .append("Cause: ").append(e.getMessage()).append("\n");
            }
        }
        if (!builder.toString().isEmpty()) {
            this.view.showException(
                    "Could not save all Infos",
                    "The following videos could not be updated:",
                    builder.toString()
            );
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
                    this.view.showException(
                            "Could not get Playlist",
                            "Please make sure youtube-dl is installed and in your PATH, you have an internet connection, and that the playlist your trying to download is not private.",
                            throwable.getMessage());
                })
                .whenCompletedSuccessful(basicVideoInfos -> {
                    for (BasicVideoInfo basicInfo : basicVideoInfos) {
                        TrackEditorView trackView = this.view.addTrackEditor();
                        SyncPair syncPair = new SyncPair(
                                basicInfo,
                                this.audioTagger,
                                this.youtubeExtractor,
                                this.metadataFinders
                        );
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
