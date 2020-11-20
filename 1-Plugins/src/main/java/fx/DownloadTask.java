package fx;

import anotherpackage.YoutubeRequestFactory;
import javafx.concurrent.Task;
import somepackage.MyDownloadProgressCallback;

public class DownloadTask<Void> extends Task<Void> implements MyDownloadProgressCallback {
    private final YoutubeRequestFactory youtubeRequestFactory;

    public DownloadTask(YoutubeRequestFactory youtubeRequestFactory) {
        this.youtubeRequestFactory = youtubeRequestFactory;
    }

    @Override
    protected Void call() throws Exception {
        this.run();
        this.youtubeRequestFactory.downloadAudio("OJdG8wsU8cw", "/tmp/", this);
        this.done();
        this.succeeded();
        return null;
    }

    @Override
    public void onProgressUpdate(float progress, long etaInSeconds) {
        this.updateProgress(progress, 100);
        System.out.println(progress + "%");
    }
}
