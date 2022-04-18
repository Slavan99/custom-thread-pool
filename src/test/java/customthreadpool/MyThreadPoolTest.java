package customthreadpool;

import customthreadpool.task.CallableTask;
import customthreadpool.worker.TaskCountingWorker;
import customthreadpool.worker.Worker;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class MyThreadPoolTest {

    private MyThreadPool myThreadPool;

    @Test
    public void testConstructorQueue() {
        myThreadPool = new MyThreadPool(5);
        assertTrue(myThreadPool.getQueue().isEmpty());
    }

    @Test
    public void testConstructorArray() {
        int numberOfThreads = 10;
        myThreadPool = new MyThreadPool(numberOfThreads);
        assertEquals(numberOfThreads, myThreadPool.getWorkers().length);
    }

    @Test
    public void testConstructorArrayNegativeSizeException() {
        int negativeSize = -5;
        Exception exception = assertThrows(NegativeArraySizeException.class, () ->
                myThreadPool = new MyThreadPool(negativeSize)
        );
        String message = exception.getMessage();
        assertEquals(Integer.toString(negativeSize), message);
    }

    @Test
    public void testShutdownNow() throws InterruptedException {
        myThreadPool = new MyThreadPool(5);
        Thread.sleep(1000);
        myThreadPool.shutdownNow();
        Arrays.stream(myThreadPool.getWorkers()).forEach(thread -> assertEquals(
                Thread.State.TERMINATED,
                thread.getState()));
    }

    @Test
    public void testSubmit() {
        myThreadPool = new MyThreadPool(5);
        int processors = Runtime.getRuntime().availableProcessors();
        CallableTask<Integer> callableTask = new CallableTask<>(() -> processors, 0);
        int result = myThreadPool.submit(callableTask);
        assertEquals(processors, result);
    }

    @Test
    public void testWorkload() {
        myThreadPool = new MyThreadPool(5);
        int taskAmount = 1000;

        for (int i = 0; i < taskAmount; i++) {
            int finalI = i;
            CallableTask<Integer> callableTask = new CallableTask<>(() -> finalI, 0);
            myThreadPool.submit(callableTask);
        }

        List<Integer> taskCountList = Arrays.stream(myThreadPool.getWorkers())
                .map(TaskCountingWorker::getCompletedTasksCount).collect(Collectors.toList());
        int minTaskCompleted = taskCountList.stream().min(Integer::compareTo).get();
        int maxTaskCompleted = taskCountList.stream().max(Integer::compareTo).get();
        assertTrue(taskAmount * 0.05 > maxTaskCompleted - minTaskCompleted);
    }


}