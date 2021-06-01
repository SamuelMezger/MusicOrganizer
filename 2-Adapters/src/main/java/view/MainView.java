package view;

import event.GuiEventHandler;

public interface MainView {

    TrackEditorView addTrackEditor();

    public void pleaseComplainAboutNotFxThread();

    void setGetPLButtonListener(GuiEventHandler handler);

    void setSaveButtonListener(GuiEventHandler handler);

    void disableDownloadButton();

    void enableDownloadButton();

    void showException(String title, String message, String details);

    String getPlUrl();
}
