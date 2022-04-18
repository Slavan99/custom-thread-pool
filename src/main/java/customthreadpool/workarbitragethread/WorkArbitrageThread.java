package customthreadpool.workarbitragethread;

import customthreadpool.MyThreadPool;
import customthreadpool.task.Task;
import customthreadpool.worker.Worker;

import java.util.ArrayDeque;
import java.util.Random;

// A special daemon thread for distributing tasks to workers
public class WorkArbitrageThread extends Thread {

    private final MyThreadPool myThreadPool;

    private final Random random;

    public WorkArbitrageThread(MyThreadPool myThreadPool) {
        this.myThreadPool = myThreadPool;
        this.random = new Random();
        setDaemon(true);
    }


    @Override
    public void run() {
        while (true) {
            ArrayDeque<Task> queue = myThreadPool.getQueue();
            Task task;

            // Synchronize in queue
            // If the queue is empty, wait
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                task = queue.poll();
            }
            if (task != null) {
                distributeTask(task);
            }
        }
    }

    private void distributeTask(Task task) {
        Worker[] workers = myThreadPool.getWorkers();
        int poolSize = workers.length;
        // Randomly select a worker from the array
        // If a worker is busy with a task, search until we find a free one
        Worker selectedWorker;
        do {
            int randomThreadIndex = random.nextInt(poolSize);
            selectedWorker = workers[randomThreadIndex];
        }
        while (selectedWorker.getCurrentTask() != null);


        selectedWorker.setTask(task);
    }

}
