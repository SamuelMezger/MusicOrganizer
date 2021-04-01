package fx;

import control.TaskFactory;
import javafx.application.Platform;

public class FxTaskFactory implements TaskFactory {

    @Override
    public void runInUiThread(Runnable task) {
        Platform.runLater(task);
    }
}
