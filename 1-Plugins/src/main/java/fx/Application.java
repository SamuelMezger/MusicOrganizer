package fx;


import control.TaskFactory;
import control.Controller;
import extraction.Downloader;
import extraction.MetadataFinder;
import extraction.iTunesMetadataFinder;
import javafx.application.Platform;
import javafx.stage.Stage;
import json.JsonParserI;
import nanojson.JsonParserAdapter;
import okhttp.OkHttpDownloader;
import extraction.youtube.JacksonVideoInfoParser;
import extraction.youtube.SapherYoutubeExtractor;
import extraction.youtube.SapherYoutubeRequestFactory;
import control.TaskManager;
import extraction.YoutubeExtractor;

import java.util.Arrays;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        Downloader downloader = new OkHttpDownloader();
        JsonParserI jsonParser = new JsonParserAdapter();
        YoutubeExtractor youtubeExtractor = new SapherYoutubeExtractor(new SapherYoutubeRequestFactory(), new JacksonVideoInfoParser(), downloader);


        List<MetadataFinder> metadataFinders = Arrays.asList(
                new iTunesMetadataFinder(downloader, jsonParser)
        );


        TaskFactory taskFactory = new FxTaskFactory();
        TaskManager taskManager = new TaskManager(taskFactory, 5);

        MainWindow view = new MainWindow();
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
            Controller controller = new Controller(view, taskManager, youtubeExtractor, metadataFinders);
        });

    }
}
