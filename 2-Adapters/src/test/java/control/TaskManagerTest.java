package control;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import use_cases.UiThread;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;


public class TaskManagerTest {

    private TaskManager taskManager;

    @Before
    public void setUp() {
        this.taskManager = new TaskManager(new MockUiThread(), 5);
    }

    @Test
    public void doInBackgroundTest() throws ExecutionException, InterruptedException {
        this.taskManager
                .doInBackground(() -> 42)
                .ifItFailsHandle(throwable -> Assert.fail())
                .whenCompletedSuccessful(x -> Assert.assertEquals(Integer.valueOf(42), x))
                .submit();
    }

    @Test
    public void doInBackgroundExceptionTest() {
        this.taskManager
                .doInBackground(() -> {
                    throw new CompletionException(new Exception());
                })
                .ifItFailsHandle(throwable -> {})
                .whenCompletedSuccessful(ignored -> Assert.fail())
                .submit();
    }

    private static class MockUiThread implements UiThread {
        @Override
        public void run(Runnable task) {
            task.run();
        }
    }
}