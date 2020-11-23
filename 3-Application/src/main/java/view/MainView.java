package view;

import event.GuiEventHandler;
import somepackage.MyDownloadProgressCallback;

public interface MainView extends MyDownloadProgressCallback {
    void addDownloadButtonListener(GuiEventHandler handler);
}
