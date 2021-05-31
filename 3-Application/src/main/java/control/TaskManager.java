package control;

import extraction.MyDownloadProgressCallback;

import java.util.concurrent.*;
import java.util.function.Supplier;


public class TaskManager {
    private final ExecutorService executor;
    private final UiThread uiThread;

    public TaskManager(UiThread uiThread, int numberOfThreads) {
        this.executor = Executors.newFixedThreadPool(numberOfThreads);
        this.uiThread = uiThread;
    }

    public void addTask(Runnable task) {
        this.executor.submit(task);
    }

    public <T> CreateTask<T> doInBackground(Supplier<T> task) {
        return CreateTask.doInBackground(task, this.executor, this.uiThread);
    }

    abstract class SupplierWithProgress<T> implements MyDownloadProgressCallback, Supplier<T> {

        private final MyDownloadProgressCallback callback;

        public SupplierWithProgress(MyDownloadProgressCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onProgressUpdate(float progress, long etaInSeconds) {
            TaskManager.this.runInUiThread(() -> this.callback.onProgressUpdate(progress, etaInSeconds));
        }
    }

    private void runInUiThread(Runnable runnable){
        this.uiThread.run(runnable);
    }


    public void shutdown() {
        this.executor.shutdownNow();
    }
}
