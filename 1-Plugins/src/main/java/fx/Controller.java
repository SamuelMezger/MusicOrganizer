package fx;

import somepackage.YoutubeException;
import somepackage.YoutubeExtractor;
import sapher.JacksonVideoInfoParser;
import sapher.SapherYoutubeExtractor;
import sapher.SapherYoutubeRequestFactory;

public class Controller {
    private final YoutubeExtractor youtubeExtractor;
    private final String destinationFolder;
    private final String videoId;
    private final HelloWorld view;
    private final BackgroundTaskFactory backgroundTaskFactory;

    public Controller(HelloWorld view) {
        this.view = view;
        this.youtubeExtractor = new SapherYoutubeExtractor(new SapherYoutubeRequestFactory(), new JacksonVideoInfoParser());
        this.destinationFolder = "/tmp/test/";
        this.videoId = "OJdG8wsU8cw";
        this.backgroundTaskFactory = new FxBackgroundTaskFactory();
        
        this.view.addDownloadButtonListener(actionEvent -> {
            this.startDownload();
        });
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
