package fx;


import control.Controller;
import extraction.youtube.NanoBasicVideoInfoParser;
import use_cases.UiThread;
import extraction.Downloader;
import extraction.MetadataFinder;
import extraction.iTunesMetadataFinder;
import javafx.application.Platform;
import javafx.stage.Stage;
import extraction.json.JsonParserI;
import nanojson.JsonParserAdapter;
import okhttp.OkHttpDownloader;
import extraction.youtube.YtDLYoutubeExtractor;
import extraction.youtube.SapherYoutubeRequestFactory;
import control.TaskManager;
import extraction.YoutubeExtractor;

import java.util.Arrays;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        Downloader downloader = new OkHttpDownloader();
        JsonParserI jsonParser = new JsonParserAdapter();
        YoutubeExtractor youtubeExtractor = new YtDLYoutubeExtractor(new SapherYoutubeRequestFactory(), new NanoBasicVideoInfoParser(jsonParser), downloader);


        List<MetadataFinder> metadataFinders = Arrays.asList(
                new iTunesMetadataFinder(downloader, jsonParser)
        );


        UiThread uiThread = new FxUiThread();
        TaskManager taskManager = new TaskManager(uiThread, 5);

        MainWindow view = new MainWindow();

        Platform.startup(() -> {
//            create primary stage
            Stage stage = new Stage();
            stage.setOnCloseRequest(windowEvent -> taskManager.shutdown());
            view.start(stage);
            new Controller(view, taskManager, youtubeExtractor, metadataFinders);
        });

    }
}
