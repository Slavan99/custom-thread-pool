package customthreadpool;

import customthreadpool.task.CallableTask;
import customthreadpool.task.RunnableTask;

public class Main {

    public static void main(String[] args) throws Exception {
        MyThreadPool threadPool = new MyThreadPool(5);
        // Runnable-based task, returns nothing, so just submit
        RunnableTask runnableTask = new RunnableTask(() -> System.out.println("SSS"), 1000);
        threadPool.submit(runnableTask);

        // Callable-based task, save the result of the submission
        CallableTask<Integer> callableTask = new CallableTask<>(() ->
                Runtime.getRuntime().availableProcessors(), 2000);
        int result = threadPool.submit(callableTask);
        System.out.println(result);

        Thread.sleep(5000);

        threadPool.shutdownNow();

    }

}
