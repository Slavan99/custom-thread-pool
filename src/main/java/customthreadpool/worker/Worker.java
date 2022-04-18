package customthreadpool.worker;

import customthreadpool.task.Task;

import java.util.concurrent.Callable;

// Worker-thread class
public class Worker extends Thread {

    private volatile boolean shutdownSignal = false;

    private final Object monitor = new Object();

    // The current task must be visible to the arbitrage thread, so volatile
    private volatile Task currentTask;

    public void setShutdownSignal(boolean shutdownSignal) {
        this.shutdownSignal = shutdownSignal;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setTask(Task task) {
        currentTask = task;
        freeWorker();
    }

    @Override
    public void run() {
        while (isRunning()) {
            waitForTask();

            // If task is set
            if (currentTask != null) {

                // Complete it
                completeCurrentTask();

                // Then reset task
                currentTask = null;

            }

        }
    }

    protected void completeCurrentTask() {
        Callable currentTaskCallableTask = currentTask.getCallable();
        // The thread sleeps for a specified time, and then saves the result of calculations
        try {
            Thread.sleep(currentTask.getTimeout());
            Object result = currentTaskCallableTask.call();
            currentTask.setResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void freeWorker() {
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }

    private boolean isRunning() {
        return !shutdownSignal;
    }

    private void waitForTask() {
        synchronized (monitor) {
            try {
                monitor.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
