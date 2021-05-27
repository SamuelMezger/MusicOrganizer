package control;

import extraction.MyDownloadProgressCallback;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class TaskManager {
    private final ExecutorService executor;
    private final TaskFactory taskFactory;

    public TaskManager(TaskFactory taskFactory) {
        this.executor = Executors.newFixedThreadPool(5);
        this.taskFactory = taskFactory;
    }

    public void addTask(Runnable task) {
        this.executor.submit(task);
    }

    public <T> TaskBuilder<T> doInBackground(Supplier<T> task) {
        return new TaskBuilder<T>(task).setExecutor(this.executor);
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

    class TaskBuilder<T> {
        private Supplier<T> task;
        private Consumer<? super T> onCompletedUiAction;
        private Consumer<? super Throwable> exceptionHandler;
        private Executor executor;

        public TaskBuilder(Supplier<T> task) {
            this.task = task;
        }

        public TaskBuilder<T> setExecutor(Executor executor) {
            this.executor = executor;
            return this;
        }

        public TaskBuilder<T> doInBackground(Supplier<T> task) {
            this.task = task;
            return this;
        }

        public TaskBuilder<T> whenCompletedSuccessful(Consumer<? super T> onCompletedUiAction) {
            this.onCompletedUiAction = onCompletedUiAction;
            return this;
        }

        public TaskBuilder<T> ifItFailsHandle(Consumer<? super Throwable> exceptionHandler) {
            this.exceptionHandler = exceptionHandler;
            return this;
        }

        public void submit() {
            CompletableFuture.supplyAsync(this.task, this.executor)
                    .whenComplete((t, throwable) -> this.runInUiThread(() -> {
                        if (throwable != null) this.exceptionHandler.accept(throwable);
                        else Optional.ofNullable(this.onCompletedUiAction).ifPresent(consumer -> consumer.accept(t));
                    }));
        }

        public void runInUiThread(Runnable runnable) {
            TaskManager.this.runInUiThread(runnable);
        }
    }

    private void runInUiThread(Runnable runnable){
        this.taskFactory.runInUiThread(runnable);
    }


    public void shutdown() {
        this.executor.shutdownNow();
    }
}
