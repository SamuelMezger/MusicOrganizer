package fx;

import event.GuiEventHandler;
import javafx.beans.value.ChangeListener;
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
import java.util.Optional;
import java.util.ResourceBundle;


public class FxTrackEditorView extends VBox implements TrackEditorView, Initializable {

    @FXML private FxTrackEditorView root;
    @FXML private Button albumCoverButton;
    @FXML private ImageView albumCoverImageView;
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
    private final FxResultsView fxResultsView;
    private GuiEventHandler metadataChangedListener;
    private BufferedImage albumCoverImage;

    public FxTrackEditorView() {
        this.popup = new Popup();
        this.fxResultsView = new FxResultsView();
        this.popup.getContent().add(this.fxResultsView);
    }

    @Override
    public FxResultsView getResultChooser() {
        return this.fxResultsView;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.configurePopup();
        ChangeListener<String> changeListener = (observableValue, oldValue, newValue) -> this.someValueChanged();
        this.trackTitleField.textProperty().addListener(changeListener);
        this.artistField.textProperty().addListener(changeListener);
        this.albumField.textProperty().addListener(changeListener);
        this.genreField.textProperty().addListener(changeListener);

        this.trackNumberField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                this.trackNumberField.setText(newValue.replaceAll("[^\\d]", ""));
                this.someValueChanged();
            }
        });
        this.releaseYearField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                this.releaseYearField.setText(newValue.replaceAll("[^\\d]", ""));
                this.someValueChanged();
            }
        });

    }

    private void someValueChanged() {
        Optional.ofNullable(this.metadataChangedListener).ifPresent(GuiEventHandler::handle);
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
    public void setEditorValuesChangedListener(GuiEventHandler listener) {
        this.metadataChangedListener = listener;
    }

    @Override
    public void setDownloadButtonListener(GuiEventHandler listener) {
        this.downloadButton.setOnAction(evObj -> listener.handle());
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

    //    Getters & Setters for fields

    @Override
    public void setAlbumCover(BufferedImage cover) {
        this.albumCoverImage = cover;
        Image fxImage = SwingFXUtils.toFXImage(cover, null);
        this.albumCoverImageView.setImage(fxImage);
    }

    @Override
    public Optional<BufferedImage> getAlbumCover() {
        return Optional.ofNullable(this.albumCoverImage);
    }

    @Override
    public void setTitle(String title) {
        this.trackTitleField.setText(title);
    }

    @Override
    public String getTitle() {
        return this.trackTitleField.getText();
    }

    @Override
    public void setArtist(String artist) {
        this.artistField.setText(artist);
    }

    @Override
    public String getArtist() {
        return this.artistField.getText();
    }

    @Override
    public void setAlbum(String album) {
        this.albumField.setText(album);
    }

    @Override
    public String getAlbum() {
        return this.albumField.getText();
    }

    @Override
    public void setTrackNumber(String trackNumber) {
        this.trackNumberField.setText(trackNumber);
    }

    @Override
    public String getTrackNumber() {
        return this.trackNumberField.getText();
    }

    @Override
    public void setReleaseYear(String releaseYear) {
        this.releaseYearField.setText(releaseYear);
    }

    @Override
    public String getReleaseYear() {
        return this.releaseYearField.getText();
    }

    @Override
    public void setGenre(String genre) {
        this.genreField.setText(genre);
    }

    @Override
    public String getGenre() {
        return this.genreField.getText();
    }
}
