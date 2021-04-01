package fx;


import control.TaskFactory;
import control.Controller;
import javafx.application.Platform;
import javafx.stage.Stage;
import sapher.JacksonVideoInfoParser;
import sapher.SapherYoutubeExtractor;
import sapher.SapherYoutubeRequestFactory;
import control.TaskManager;
import somepackage.YoutubeExtractor;

public class Application {

    public static void main(String[] args) {
        
        MainWindow view = new MainWindow();
        YoutubeExtractor youtubeExtractor = new SapherYoutubeExtractor(new SapherYoutubeRequestFactory(), new JacksonVideoInfoParser());
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
            Controller controller = new Controller(view, taskManager, taskFactory, youtubeExtractor);
        });

    }
}
