package fx;

import event.GuiEventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.embed.swing.SwingFXUtils;
import view.TrackEditorView;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;


public class FxTrackEditorView extends VBox implements TrackEditorView, Initializable {

    @FXML private ImageView albumCoverImageView;
    @FXML private FxTrackEditorView root;
    @FXML private Button albumCoverButton;
    @FXML private Label videoTitleLabel;
    @FXML private TextField trackTitleField;
    @FXML private TextField artistField;
    @FXML private TextField albumField;
    @FXML private TextField trackNumberField;
    @FXML private TextField releaseYearField;
    @FXML private TextField genreField;
    @FXML private Button refreshButton;
    @FXML private Button searchButton;
    @FXML private Button openWebsite;
    @FXML public Button downloadButton;
    @FXML private ProgressBar downloadProgressBar;
    private final Popup popup;

    public FxTrackEditorView() {
        this.popup = new Popup();
        this.popup.getContent().add(new ResultsView());

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.configurePopup();
    }

    public void configurePopup() {
        this.albumCoverButton.focusedProperty().addListener((observableValue, isNotFocused, isFocused) -> {
            if (isNotFocused) this.popup.hide();
        });
        this.albumCoverButton.setOnAction(actionEvent -> {
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
        Point2D point = this.albumCoverButton.localToScene(0.0,  0.0);
        this.popup.setX(window.getX() + point.getX());
        this.popup.setY(window.getY() + point.getY() + 1.5 * this.albumCoverButton.getHeight());
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
    public void setReleaseYear(Integer releaseYear) {
        this.releaseYearField.setText(releaseYear.toString());
    }

    @Override
    public void setAlbumCover(BufferedImage cover) {
        Image fxImage = SwingFXUtils.toFXImage(cover, null);
        this.albumCoverImageView.setImage(fxImage);
    }
}
