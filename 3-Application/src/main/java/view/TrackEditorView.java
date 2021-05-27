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

    void setAlbum(String album);

    void setTitle(String title);

    void setArtist(String artist);

    void setReleaseYear(Integer releaseYear);

    void setAlbumCover(BufferedImage cover);
}
