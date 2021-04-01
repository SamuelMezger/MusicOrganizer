package control;

public interface TaskFactory {
    void runInUiThread(Runnable task);
}
