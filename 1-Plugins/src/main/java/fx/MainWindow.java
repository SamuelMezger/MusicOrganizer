package fx;

import event.GuiEventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import view.MainView;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.TrackEditorView;

import java.io.IOException;


public class MainWindow extends Application implements MainView {
    private TextField playListUrl;
    private VBox root;
    private VBox trackListPane;
    private Button syncPLButton;


    @Override
    public void start(Stage primaryStage) {
        this.playListUrl = new TextField("PLvy1DvHP0feqGoG474BC0lTTjMNjroK1R");
        this.syncPLButton = new Button("Sync Playlist");

        primaryStage.setTitle("Hello World!");
        this.root = new VBox();

        Button btn = new Button("Say 'Hello World'");
        btn.setOnAction(event -> System.out.println("Hello World!"));

        TopBar topBar = new TopBar();
        ObservableList<Node> topBarChildren = topBar.getChildren();
        topBarChildren.add(btn);
        topBarChildren.add(this.playListUrl);
        topBarChildren.add(this.syncPLButton);
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
    public void addGetPLButtonListener(GuiEventHandler listener) {
        this.syncPLButton.setOnAction(listener::handle);
    }

    @Override
    public void disableDownloadButton() {
        this.syncPLButton.setDisable(true);
    }

    @Override
    public void enableDownloadButton() {
        this.syncPLButton.setDisable(false);
    }

    @Override
    public void showCouldNotGetPlaylistException(String title, String message, String details) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message + "\n\n" + details);
        alert.setTitle(title);
        alert.show();
    }

    @Override
    public String getPlUrl() {
        return this.playListUrl.getText();
    }

    @Override
    public TrackEditorView addTrackEditor() {

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/TrackEditor.fxml"));
        try {
            this.trackListPane.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader.<FxTrackEditorView>getController();
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
