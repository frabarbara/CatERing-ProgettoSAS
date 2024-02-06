package businesslogic.task;

import businesslogic.turn.Turn;

public interface TaskEventReceiver {

    public void updateTaskAdded(TaskSheet ts, Task t);
    public void updateTasksRearranged(TaskSheet ts);
    public void updateTaskScheduled(Task task, Turn turn);

}
