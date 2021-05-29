package view;

import event.GuiEventHandler;
import extraction.MyDownloadProgressCallback;

import java.awt.image.BufferedImage;

public interface TrackEditorView extends MyDownloadProgressCallback {

    void showVideoNotAvailable(String errorMessage);

    void addDownloadButtonListener(GuiEventHandler listener);

    void disableDownloadButton();

    void enableDownloadButton();

    void setVideoTitle(String videoTitle);

    void setAlbumCover(BufferedImage cover);

    void setTitle(String title);

    void setArtist(String artist);

    void setAlbum(String album);

    void setTrackNumber(Integer releaseYear);

    void setReleaseYear(Integer releaseYear);

    void setGenre(String genre);
}
