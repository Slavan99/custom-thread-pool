package customthreadpool.worker;

import customthreadpool.task.Task;

public class TaskCountingWorker extends Worker {

    private volatile int completedTasksCount = 0;

    public TaskCountingWorker() {
        super();
    }

    public int getCompletedTasksCount() {
        return completedTasksCount;
    }

    @Override
    protected void completeCurrentTask() {
        super.completeCurrentTask();
        completedTasksCount++;
    }
}
