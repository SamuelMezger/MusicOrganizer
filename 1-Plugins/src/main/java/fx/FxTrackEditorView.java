package fx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import somepackage.BasicVideoInfo;
import view.TrackEditorView;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class FxTrackEditorView extends GridPane implements TrackEditorView {
    
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
    @FXML private ProgressBar downloadProgressBar;


//    public FxTrackEditorView(BasicVideoInfo basicVideoInfo) {
//        this.getChildren().add(new Text(basicVideoInfo.getVideoTitle()));
//    }


    public void changeText(){
        this.videoTitleLabel.setText("some other text");
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
