package businesslogic.task;

import businesslogic.turn.Turn;
import businesslogic.user.Cook;

public interface TaskEventReceiver {

    public void updateTaskAdded(TaskSheet ts, Task t);
    public void updateTasksRearranged(TaskSheet ts);
    public void updateTaskScheduled(Task task, Turn turn);
    public void updateCookAssigned(Task t, Cook c);

}
