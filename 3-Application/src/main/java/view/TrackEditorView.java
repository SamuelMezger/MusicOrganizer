package view;

import event.GuiEventHandler;
import extraction.MyDownloadProgressCallback;

import java.awt.image.BufferedImage;
import java.util.Optional;

public interface TrackEditorView extends MyDownloadProgressCallback {

    void showVideoNotAvailable(String errorMessage);

    ResultsView getResultChooser();

    void setEditorValuesChangedListener(GuiEventHandler listener);

    void setDownloadButtonListener(GuiEventHandler listener);

    void disableDownloadButton();

    void enableDownloadButton();

//    TODO split interface into TrackView and TrackEditorView

    void setVideoTitle(String videoTitle);

    void setAlbumCover(BufferedImage cover);

    Optional<BufferedImage> getAlbumCover();

    void setTitle(String title);

    String getTitle();

    void setArtist(String artist);

    String getArtist();

    void setAlbum(String album);

    String getAlbum();

    void setTrackNumber(String releaseYear);

    String getTrackNumber();

    void setReleaseYear(String releaseYear);

    String getReleaseYear();

    void setGenre(String genre);

    String getGenre();
}
