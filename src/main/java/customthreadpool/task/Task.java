package customthreadpool.task;

import java.util.concurrent.Callable;

// Common task interface
public interface Task<T> {

    Callable<T> getCallable();

    long getTimeout();

    T getResult();

    void setResult(T result);

}
