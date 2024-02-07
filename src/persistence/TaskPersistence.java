package persistence;

import businesslogic.event.ServiceInfo;
import businesslogic.task.Task;
import businesslogic.task.TaskEventReceiver;
import businesslogic.task.TaskSheet;
import businesslogic.turn.Turn;
import businesslogic.user.Cook;

public class TaskPersistence implements TaskEventReceiver {

    @Override
    public void updateTaskAdded(TaskSheet ts, Task t) {
        int pos = ts.getTaskPosition(t);
        Task.saveNewTask(ts.getId(), t, pos);
    }

    @Override
    public void updateTasksRearranged(TaskSheet ts) { TaskSheet.saveTasksOrder(ts); }

    @Override
    public void updateTaskScheduled(Task task, Turn turn) {
        Task.saveSchedule(task, turn);
    }

    @Override
    public void updateCookAssigned(Task t, Cook c) {
        Task.saveCookAssigned(t, c);
        // Cook.removeAvailability(t.getAssignedTurn(), c);
    }

    @Override
    public void updateTaskSheetReset(ServiceInfo s) {
        Task.eraseAllServiceTasks(s);
    }

    @Override
    public void updateQtyChanged(TaskSheet ts, Task t) {
        Task.updateNewQty(ts, t);
    }

    @Override
    public void updateAvailChanged(TaskSheet ts, Task t) {
        Task.updateNewAvail(ts, t);
    }

    @Override
    public void updateTaskDeleted(TaskSheet ts, Task t) {
        Task.eraseTask(t);
    }

    @Override
    public void updateTaskRescheduled(Task task, Turn newTurn) {
        Task.saveReschedule(task, newTurn);
    }

    @Override
    public void updateTaskRemoved(Task t) {
        Task.updateRemoveTask(t);
    }
}
