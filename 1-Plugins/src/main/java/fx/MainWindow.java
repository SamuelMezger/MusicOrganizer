package fx;

import com.sun.javafx.menu.MenuItemBase;
import event.GuiEventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import view.MainView;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.TrackEditorView;

import java.io.IOException;


public class MainWindow extends Application implements MainView {
    private ProgressBar progressBar;
    private Text progressTextField;
    private Button downloadBtn;
    private VBox root;
    private VBox trackListPane;
    private Button PlDownloadBtn;


    @Override
    public void start(Stage primaryStage) {
        this.progressBar = new ProgressBar(0);
        this.progressTextField = new Text("Not yet started");
        this.downloadBtn = new Button("Download");
        this.PlDownloadBtn = new Button("Sync Playlist");

        primaryStage.setTitle("Hello World!");
        this.root = new VBox();

        Button btn = new Button("Say 'Hello World'");
        btn.setOnAction(event -> System.out.println("Hello World!"));

        TopBar topBar = new TopBar();
        ObservableList<Node> topBarChildren = topBar.getChildren();
        topBarChildren.add(btn);
        topBarChildren.add(this.downloadBtn);
        topBarChildren.add(this.progressBar);
        topBarChildren.add(this.progressTextField);
        topBarChildren.add(this.PlDownloadBtn);
        this.root.getChildren().add(topBar);

        this.trackListPane = new VBox();
        this.trackListPane.setPadding(new Insets(5, 5, 5, 5));
        this.trackListPane.setSpacing(5);

        ScrollPane scrollPane = new ScrollPane(this.trackListPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        VBox.setVgrow(scrollPane, Priority.SOMETIMES);
        this.root.getChildren().add(scrollPane);
        
        primaryStage.setScene(new Scene(this.root, 600, 500));
        primaryStage.show();
//        this.pleaseComplainAboutNotFxThread();
    }


    @Override
    public void onProgressUpdate(float progress, long etaInSeconds) {
        System.out.println(progress);
        System.out.println(Thread.currentThread().getName());
        this.progressBar.setProgress(progress/100);
        this.progressTextField.setText(progress + "%");
    }


    public void changeText(String text) {
        this.progressTextField.setText(text);
    }

    @Override
    public void addDownloadButtonListener(GuiEventHandler listener) {
        this.downloadBtn.setOnAction(listener::handle);
    }

    @Override
    public void addGetPLButtonListener(GuiEventHandler listener) {
        this.PlDownloadBtn.setOnAction(listener::handle);
    }

    @Override
    public TrackEditorView addTrackEditor() {

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../view/TrackEditor.fxml"));
        try {
            this.trackListPane.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loader.<FxTrackEditorView>getController();
    }

    public void addProgressBarUpdater(ObservableValue<? extends Number> progressProperty){
        this.progressBar.progressProperty().bind(progressProperty);
    }

    @Override
    public void pleaseComplainAboutNotFxThread() {
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " adds button");
            this.root.getChildren().add(new Button("can u see me"));
            System.out.println(Thread.currentThread().getName() + " added button");
        }).start();
    }

}
