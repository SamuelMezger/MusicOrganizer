package fx;

import control.BackgroundTaskFactory;
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
}
