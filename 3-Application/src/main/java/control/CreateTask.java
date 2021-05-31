package control;


import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class CreateTask<T> {

    private final Supplier<T> task;
    private final Executor executor;
    private final UiThread uiThread;
    private Consumer<? super T> onCompletedUiAction;
    private Consumer<? super Throwable> exceptionHandler;


    private CreateTask(Supplier<T> task, Executor executor, UiThread uiThread) {
        this.task = task;
        this.executor = executor;
        this.uiThread = uiThread;
    }

    public static <U> CreateTask<U> doInBackground(Supplier<U> task, Executor executor, UiThread uiThread) {
        return new CreateTask<>(task, executor, uiThread) ;
    }

    public SubmitableTask<T> ifItFailsHandle(Consumer<? super Throwable> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return new SubmitableTask<>();
    }

    public class SubmitableTask<U> {
        private SubmitableTask() {}

        public SubmitableTask<U> whenCompletedSuccessful(Consumer<? super T> onCompletedUiAction) {
            CreateTask.this.onCompletedUiAction = onCompletedUiAction;
            return this;
        }

        public void submit() {
            CompletableFuture.supplyAsync(CreateTask.this.task, CreateTask.this.executor)
                    .whenComplete((t, throwable) -> CreateTask.this.uiThread.run(() -> {
                        if (throwable != null) CreateTask.this.exceptionHandler.accept(throwable.getCause());
                        else Optional.ofNullable(CreateTask.this.onCompletedUiAction).ifPresent(consumer -> consumer.accept(t));
                    }));
        }
    }
}