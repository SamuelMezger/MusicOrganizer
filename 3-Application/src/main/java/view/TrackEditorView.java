package view;

import event.GuiEventHandler;
import somepackage.MyDownloadProgressCallback;

public interface TrackEditorView extends MyDownloadProgressCallback {

    void showVideoNotAvailable(String errorMessage);

    void addDownloadButtonListener(GuiEventHandler listener);

    void disableDownloadButton();

    void enableDownloadButton();

    void setVideoTitle(String videoTitle);

    void setAlbum(String album);

    void setTitle(String title);

    void setArtist(String artist);

    void setReleaseDate(String releaseDate);
}
