package fx;

import anotherpackage.View;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import somepackage.MyDownloadProgressCallback;


public class HelloWorld extends Application implements MyDownloadProgressCallback, View {
    private ProgressBar progressBar;
    private Text progressTextField;
    private Stage primaryStage;
    private Button downloadBtn;
    

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.progressBar = new ProgressBar(0);
        this.progressTextField = new Text("Not yet started");
        this.downloadBtn = new Button("Download");
        
        this.primaryStage.setTitle("Hello World!");
        Button btn = new Button("Say 'Hello World'");
        btn.setOnAction(event -> System.out.println("Hello World!"));

        VBox vBox = new VBox();
        vBox.getChildren().add(btn);
        vBox.getChildren().add(this.downloadBtn);
        vBox.getChildren().add(this.progressBar);
        vBox.getChildren().add(this.progressTextField);
        StackPane root = new StackPane();
        root.getChildren().add(vBox);
        this.primaryStage.setScene(new Scene(root, 300, 250));
        new Controller(this);
        this.primaryStage.show();
    }


    @Override
    public void onProgressUpdate(float progress, long etaInSeconds) {
        System.out.println(progress);
        this.progressBar.setProgress(progress);
        this.progressTextField.setText(progress + "%");
    }


    public void changeText(String text) {
        this.progressTextField.setText(text);
    }

//    @Override
    public void addDownloadButtonListener(EventHandler<ActionEvent> listener) {
        this.downloadBtn.setOnAction(listener);
    }
    
    public void addProgressBarUpdater(ObservableValue<? extends Number> progressProperty){
        this.progressBar.progressProperty().bind(progressProperty);
    }
    
}
