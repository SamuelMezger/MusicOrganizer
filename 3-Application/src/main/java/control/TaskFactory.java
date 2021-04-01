package control;

public interface TaskFactory {
    Runnable createTask(Runnable target);

    void runInUiThread(Runnable task);
}
