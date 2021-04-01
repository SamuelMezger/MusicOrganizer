package fx;

import event.GuiEventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.embed.swing.SwingFXUtils;
import view.TrackEditorView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class FxTrackEditorView extends VBox implements TrackEditorView, Initializable {

    @FXML private FxTrackEditorView root;
    @FXML private Button albumCover;
    @FXML private Label videoTitleLabel;
    @FXML private TextField trackTitleField;
    @FXML private TextField artistField;
    @FXML private TextField albumField;
    @FXML private TextField trackNumberField;
    @FXML private TextField releaseDateField;
    @FXML private TextField genreField;
    @FXML private Button refreshButton;
    @FXML private Button searchButton;
    @FXML private Button openWebsite;
    @FXML public Button downloadButton;
    @FXML private ProgressBar downloadProgressBar;
    private final Popup popup;

    public FxTrackEditorView() {
        this.popup = new Popup();
        GridPane popupContent = new GridPane();
        popupContent.add(new Button("lulu"), 1, 1);
        popupContent.add(new Label("kuku"), 2, 1);
        this.popup.getContent().add(popupContent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.configurePopup();
    }

    public void configurePopup() {
        this.albumCover.focusedProperty().addListener((observableValue, isNotFocused, isFocused) -> {
            if (isNotFocused) this.popup.hide();
        });
        this.albumCover.setOnAction(actionEvent -> {
            if (this.popup.isShowing()) {
                this.popup.hide();
            } else {
                this.showPopup();
            }
        });
    }

    private void showPopup() {
        Window window = this.root.getScene().getWindow();
        this.popup.show(window);
        Point2D point = this.albumCover.localToScene(0.0,  0.0);
        this.popup.setX(window.getX() + point.getX());
//        System.out.println("popup :" + this.popup.getHeight());
//        System.out.println("button:" + this.albumCover.getHeight());
        this.popup.setY(window.getY() + point.getY() + this.albumCover.getHeight() + this.popup.getHeight());
    }

    @Override
    public void addDownloadButtonListener(GuiEventHandler listener) {
        this.downloadButton.setOnAction(listener::handle);
    }


    @Override
    public void disableDownloadButton() {
        this.downloadButton.setDisable(true);
    }

    @Override
    public void enableDownloadButton() {
        this.downloadButton.setDisable(false);
    }

    @Override
    public void showVideoNotAvailable(String errorMessage) {
        Label errorLabel = new Label(errorMessage);
        errorLabel.setTextFill(Color.RED);
        this.root.getChildren().add(errorLabel);
    }

    @Override
    public void onProgressUpdate(float progress, long etaInSeconds) {
        this.downloadProgressBar.setProgress(progress);
    }


    @Override
    public void setVideoTitle(String videoTitle) {
        this.videoTitleLabel.setText(videoTitle);
    }

    @Override
    public void setAlbum(String album) {
        this.albumField.setText(album);
    }

    @Override
    public void setTitle(String title) {
        this.trackTitleField.setText(title);
    }

    @Override
    public void setArtist(String artist) {
        this.artistField.setText(artist);
    }

    @Override
    public void setReleaseDate(String releaseDate) {
        this.releaseDateField.setText(releaseDate);
    }
}
