package customthreadpool.worker;

import customthreadpool.task.CallableTask;
import customthreadpool.task.Task;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WorkerTest {

    @Test
    public void testGetCurrentTask() {
        Worker worker = new Worker();
        Task<String> callableTask = new CallableTask<>(() -> "result", 1000);
        worker.setTask(callableTask);
        assertEquals(callableTask, worker.getCurrentTask());
    }

    @Test
    public void testGetCompletedTasksCount() throws Exception {
        TaskCountingWorker worker = new TaskCountingWorker();

        long timeout1 = 1000;
        Task<String> callableTask = new CallableTask<>(() -> "result", timeout1);
        worker.start();
        Thread.sleep(2000);
        worker.setTask(callableTask);
        long timeout2 = 2000;
        Task<Integer> callableTaskInt = new CallableTask<>(
                () -> Runtime.getRuntime().availableProcessors(),
                timeout2);
        worker.setTask(callableTaskInt);
        worker.setShutdownSignal(true);
        worker.freeWorker();

        assertEquals(2, worker.getCompletedTasksCount());

    }

}