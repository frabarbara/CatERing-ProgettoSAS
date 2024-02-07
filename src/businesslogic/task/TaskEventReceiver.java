package businesslogic.task;

import businesslogic.event.ServiceInfo;
import businesslogic.turn.Turn;
import businesslogic.user.Cook;

public interface TaskEventReceiver {

    public void updateTaskAdded(TaskSheet ts, Task t);
    public void updateTasksRearranged(TaskSheet ts);
    public void updateTaskScheduled(Task task, Turn turn);
    public void updateCookAssigned(Task t, Cook c);
    public void updateTaskSheetReset(ServiceInfo s);
    public void updateQtyChanged(TaskSheet ts, Task t);
    public void updateAvailChanged(TaskSheet ts, Task t);
    public void updateTaskDeleted(TaskSheet ts, Task t);
    public void updateTaskRescheduled(Task task, Turn newTurn);
    public void updateTaskRemoved(Task t);

}
