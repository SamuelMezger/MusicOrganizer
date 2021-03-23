package fx;

import control.BackgroundTaskFactory;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class FxBackgroundTaskFactory implements BackgroundTaskFactory {

    
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

    public <T> Runnable createResultTask(Runnable target) {
        return new Task<T>() {
            @Override
            protected T call() throws Exception {
                target.run();
                return null;
            }
        };
    }
}
