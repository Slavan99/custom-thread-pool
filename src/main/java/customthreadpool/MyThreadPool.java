package customthreadpool;

import customthreadpool.task.Task;
import customthreadpool.workarbitragethread.WorkArbitrageThread;
import customthreadpool.worker.TaskCountingWorker;
import customthreadpool.worker.Worker;

import java.util.ArrayDeque;

public class MyThreadPool {

    private final ArrayDeque<Task> queue;

    public ArrayDeque<Task> getQueue() {
        return queue;
    }

    private final TaskCountingWorker[] workers;

    public TaskCountingWorker[] getWorkers() {
        return workers;
    }


    public MyThreadPool(int numOfThreads) {
        queue = new ArrayDeque<>();
        workers = new TaskCountingWorker[numOfThreads];
        for (int i = 0; i < numOfThreads; i++) {
            workers[i] = new TaskCountingWorker();
            workers[i].start();
        }

        WorkArbitrageThread workArbitrageThread = new WorkArbitrageThread(this);
        workArbitrageThread.start();
    }

    // When a task is added to the queue, a notification is made to the arbitrage thread
    // Returns the result saved in the task
    public <T> T submit(Task<T> task) {
        synchronized (queue) {
            queue.add(task);
            queue.notifyAll();
        }
        return task.getResult();

    }

    // All worker threads finish work
    public void shutdownNow() {
        for (Worker worker : workers) {
            worker.freeWorker();
            worker.setShutdownSignal(true);
        }

    }


}
