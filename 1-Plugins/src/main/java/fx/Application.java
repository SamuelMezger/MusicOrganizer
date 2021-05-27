package fx;


import control.TaskFactory;
import control.Controller;
import extraction.Downloader;
import javafx.application.Platform;
import javafx.stage.Stage;
import okhttp.OkHttpDownloader;
import sapher.JacksonVideoInfoParser;
import sapher.SapherYoutubeExtractor;
import sapher.SapherYoutubeRequestFactory;
import control.TaskManager;
import extraction.YoutubeExtractor;

public class Application {

    public static void main(String[] args) {
        Downloader downloader = new OkHttpDownloader();
        MainWindow view = new MainWindow();
        YoutubeExtractor youtubeExtractor = new SapherYoutubeExtractor(new SapherYoutubeRequestFactory(), new JacksonVideoInfoParser(), downloader);
        TaskFactory taskFactory = new FxTaskFactory();
        TaskManager taskManager = new TaskManager(taskFactory);

        try {
            view.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Platform.startup(() -> {
//            create primary stage
            Stage stage = new Stage();
            stage.setOnCloseRequest(windowEvent -> taskManager.shutdown());
            view.start(stage);
            Controller controller = new Controller(view, taskManager, youtubeExtractor);
        });

    }
}
