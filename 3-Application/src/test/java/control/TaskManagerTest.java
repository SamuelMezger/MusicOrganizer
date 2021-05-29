package control;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;


public class TaskManagerTest {

    private TaskManager taskManager;

    @Before
    public void setUp() {
        this.taskManager = new TaskManager(new MockTaskFactory(), 5);
    }

    @Test
    public void howDoFuturesWorkTest() throws ExecutionException, InterruptedException {
        List<String> result = CompletableFuture.supplyAsync(() -> new LinkedList<String>(Arrays.asList("asdf")))
                .whenComplete((list, throwable) -> {
                    list.add("somsin els");
                }).get();
        System.out.println(result);
    }

    @Test
    public void doInBackgroundSuccessTest() {
        Result<Double> result = new Result<>();
        this.taskManager
                .doInBackground(() -> 42.0 / 2.0)
                .whenCompletedSuccessful(divisionResult -> result.value = divisionResult)
                .ifItFailsHandle(throwable -> Assert.fail())
                .submit();
    }

    @Test
    public void doInBackgroundExceptionTest() {
        this.taskManager
                .doInBackground(() -> {
                    throw new CompletionException(new Exception());
                })
                .whenCompletedSuccessful(divisionResult -> {
                    System.out.println(divisionResult);
                    Assert.assertEquals(Integer.valueOf(21), divisionResult);
                })
                .ifItFailsHandle(throwable -> {
                    System.out.println(throwable.getMessage());
                    Assert.fail();
                })
                .submit();
    }

    class Result<T> {
        Throwable throwable;
        T value;
    }

    private static class MockTaskFactory implements TaskFactory {
        @Override
        public void runInUiThread(Runnable task) {
            task.run();
        }
    }
}