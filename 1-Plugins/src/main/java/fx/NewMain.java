package fx;


import control.BackgroundTaskFactory;
import control.Controller;
import javafx.application.Platform;
import javafx.stage.Stage;
import sapher.JacksonVideoInfoParser;
import sapher.SapherYoutubeExtractor;
import sapher.SapherYoutubeRequestFactory;
import control.DownloadManager;
import somepackage.YoutubeExtractor;

public class NewMain {

    public static void main(String[] args) {
        
        MainWindow view = new MainWindow();
        YoutubeExtractor youtubeExtractor = new SapherYoutubeExtractor(new SapherYoutubeRequestFactory(), new JacksonVideoInfoParser());
        BackgroundTaskFactory backgroundTaskFactory= new FxBackgroundTaskFactory();
        DownloadManager dlManager = new DownloadManager(backgroundTaskFactory);

        try {
            view.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Platform.startup(() -> {
//            create primary stage
            Stage stage = new Stage();
            stage.setOnCloseRequest(windowEvent -> dlManager.shutdown());
            view.start(stage);
            Controller controller = new Controller(view, dlManager, backgroundTaskFactory, youtubeExtractor);
        });

    }
}
