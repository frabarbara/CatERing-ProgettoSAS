package businesslogic.task;

public interface TaskEventReceiver {

    public void updateTaskAdded(TaskSheet ts, Task t);
    public void updateTasksRearranged(TaskSheet ts);

}
