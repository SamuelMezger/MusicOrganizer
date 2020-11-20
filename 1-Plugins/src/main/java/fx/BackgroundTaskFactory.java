package fx;

public interface BackgroundTaskFactory {
    Runnable createTask(Runnable target);
}
