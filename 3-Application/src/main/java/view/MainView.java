package view;

import event.GuiEventHandler;
import somepackage.MyDownloadProgressCallback;

public interface MainView extends MyDownloadProgressCallback {
    void addDownloadButtonListener(GuiEventHandler handler);
    
    TrackEditorView addTrackEditor();
    public void pleaseComplainAboutNotFxThread();

    void addGetPLButtonListener(GuiEventHandler handler);

    void disableDownloadButton();

    void enableDownloadButton();

    void showCouldNotGetPlaylistException(String title, String message, String details);
}
