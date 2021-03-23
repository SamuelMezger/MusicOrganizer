package control;

import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static java.lang.Thread.currentThread;

public class DownloadManager {
    private final ExecutorService executor;
    private final BackgroundTaskFactory backgroundTaskFactory;

    public DownloadManager(BackgroundTaskFactory backgroundTaskFactory) {
        this.executor = Executors.newFixedThreadPool(5);
        this.backgroundTaskFactory = backgroundTaskFactory;
    }

    public static void main(String[] args) {
        DownloadManager dlManager = new DownloadManager(null);

        CompletableFuture<String>[] futures = new CompletableFuture[5];

        for (CompletableFuture<String> future : futures) {
            CompletableFuture<String> tmp_future = dlManager.getCompletableFuture(() -> {
                int waitingFor = (int) (Math.random() * 10000);
                System.out.println(waitingFor + "\t" + currentThread().getName() + " started");
                try {
                    Thread.sleep(waitingFor);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(waitingFor + "\t" + currentThread().getName() + " finished");
                return waitingFor + "";
            }, (string, throwable) -> {
                System.out.println(string + "\t" + currentThread().getName() + " complete");
            });
            future = tmp_future;
        }

        System.out.println(currentThread().getName() + " submitted");
    }

    public  <T> CompletableFuture<T> getCompletableFutureDLM(Supplier<T> tSupplier, BiConsumer<T, Throwable> tThrowableBiConsumer) {

        return CompletableFuture.supplyAsync(tSupplier, this.executor)
                .whenComplete((t, throwable) -> this.backgroundTaskFactory.
                        runInUiThread(new RunnableConsumer<T>(t, throwable, tThrowableBiConsumer)));
    }

    public  <T> CompletableFuture<T> getCompletableFuture(Supplier<T> tSupplier, BiConsumer<T, Throwable> tThrowableBiConsumer) {

        return CompletableFuture.supplyAsync(tSupplier, this.executor)
                .whenComplete(tThrowableBiConsumer);
    }

    private class TaskWithGuiUpdate<T>  {
        private final CompletableFuture<T> future;
        private BackgroundTaskFactory backgroundTaskFactory;

        public TaskWithGuiUpdate(BackgroundTaskFactory backgroundTaskFactory) {
            this.future = new CompletableFuture<T>();
            this.backgroundTaskFactory = backgroundTaskFactory;
        }

        public TaskWithGuiUpdate<T> andWhenCompleteUpdateGuiWith(BiConsumer<T, Throwable> tThrowableBiConsumer) {
            this.future.whenComplete((t, throwable) -> this.backgroundTaskFactory.
                    runInUiThread(new RunnableConsumer<T>(t, throwable, tThrowableBiConsumer)));
            return this;
        }
    }

    private static class RunnableConsumer<T> implements Runnable {
        private final T parameter;
        private final Throwable ex;
        private final BiConsumer<T, Throwable> consumer;

        public RunnableConsumer(T parameter, Throwable ex, BiConsumer<T, Throwable> consumer) {
            this.parameter = parameter;
            this.ex = ex;
            this.consumer = consumer;
        }

        public void run() {
            this.consumer.accept(this.parameter, this.ex);
        }
    }

    public void shutdown() {
        this.executor.shutdownNow();
    }
}
