package fx;

import javafx.concurrent.Task;
import sapher.SapherYoutubeRequestFactory;

public class Controller {
    private final SapherYoutubeRequestFactory youtubeRequestFactory;
    HelloWorld view;
    
    public Controller(HelloWorld view) {
        this.view = view;
        this.youtubeRequestFactory = new SapherYoutubeRequestFactory();
        
        
        view.addDownloadButtonListener(actionEvent -> {
            this.startDownload();
        });
    }

    private void startDownload() {
        Task<Void> task = new DownloadTask<>(this.youtubeRequestFactory);
        this.view.addProgressBarUpdater(task.progressProperty());
        new Thread(task).start();
    }
}
