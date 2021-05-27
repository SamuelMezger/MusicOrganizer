package view;

import event.GuiEventHandler;

public interface MainView {

    TrackEditorView addTrackEditor();

    public void pleaseComplainAboutNotFxThread();

    void addGetPLButtonListener(GuiEventHandler handler);

    void disableDownloadButton();

    void enableDownloadButton();

    void showCouldNotGetPlaylistException(String title, String message, String details);

    String getPlUrl();
}
