package customthreadpool.task;

import java.util.concurrent.Callable;

public class CallableTask<T> implements Task<T> {

    private final Callable<T> task;

    private final long timeout;

    private volatile T result;

    public CallableTask(Callable<T> task, long timeout) {
        this.task = task;
        this.timeout = timeout;
    }

    @Override
    public Callable<T> getCallable() {
        return task;
    }

    @Override
    public long getTimeout() {
        return timeout;
    }

    // Until the result is calculated, the thread waits
    @Override
    public synchronized T getResult() {
        while (result == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // When the result is calculated, notifies the waiting thread
    public synchronized void setResult(T result) {
        this.result = result;
        notifyAll();
    }
}
