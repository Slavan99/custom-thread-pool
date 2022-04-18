package customthreadpool.task;

import java.util.concurrent.Callable;

public class RunnableTask implements Task<Void> {

    private final Runnable task;

    private final long timeout;

    public RunnableTask(Runnable task, long timeout) {
        this.task = task;
        this.timeout = timeout;
    }

    @Override
    public Callable<Void> getCallable() {
        return () -> {
            task.run();
            return null;
        };
    }

    @Override
    public long getTimeout() {
        return timeout;
    }

    @Override
    public Void getResult() {
        // intentionally
        return null;
    }

    @Override
    public void setResult(Void result) {
        // ignored
    }


}
