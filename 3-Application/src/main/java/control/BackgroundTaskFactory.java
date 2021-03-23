package control;

public interface BackgroundTaskFactory {
    Runnable createTask(Runnable target);

    void runInUiThread(Runnable task);
}
