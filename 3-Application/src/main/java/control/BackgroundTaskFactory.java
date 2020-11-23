package control;

public interface BackgroundTaskFactory {
    Runnable createTask(Runnable target);
}
