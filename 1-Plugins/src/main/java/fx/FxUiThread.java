package fx;

import use_cases.UiThread;
import javafx.application.Platform;

public class FxUiThread implements UiThread {

    @Override
    public void run(Runnable task) {
        Platform.runLater(task);
    }
}
