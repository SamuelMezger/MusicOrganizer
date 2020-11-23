package control;

import somepackage.YoutubeException;
import somepackage.YoutubeExtractor;
import view.MainView;

public class Controller {
    private final YoutubeExtractor youtubeExtractor;
    private final String destinationFolder;
    private final String videoId;
    private final MainView view;
    private final BackgroundTaskFactory backgroundTaskFactory;

    public Controller(MainView view, BackgroundTaskFactory backgroundTaskFactory, YoutubeExtractor youtubeExtractor) {
        this.view = view;
        this.youtubeExtractor = youtubeExtractor;
        this.backgroundTaskFactory = backgroundTaskFactory;
        
        this.destinationFolder = "/tmp/test/";
        this.videoId = "OJdG8wsU8cw";
        
        this.view.addDownloadButtonListener(actionEvent -> Controller.this.startDownload());
    }

    private void startDownload() {
        Runnable downloadTask = this.backgroundTaskFactory.createTask(() -> {
            try {
                this.youtubeExtractor.downloadAudio(this.videoId, this.destinationFolder, this.view);
            } catch (YoutubeException e) {
                e.printStackTrace();
            }
        });
        
        new Thread(downloadTask).start();
    }
}
