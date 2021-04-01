package fx;

import control.TaskFactory;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class FxTaskFactory implements TaskFactory {

    @Override
    public Runnable createTask(Runnable target) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                target.run();
                return null;
            }
        };
    }

    @Override
    public void runInUiThread(Runnable task) {
        Platform.runLater(task);
    }
}
