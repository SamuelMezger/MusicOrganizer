package control;

import extraction.ProgressCallback;
import use_cases.UiThread;

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

    public abstract class SupplierWithProgress<T> implements ProgressCallback, Supplier<T> {

        private final ProgressCallback callback;

        public SupplierWithProgress(ProgressCallback callback) {
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
