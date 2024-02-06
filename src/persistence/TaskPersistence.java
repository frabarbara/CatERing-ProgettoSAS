package persistence;

import businesslogic.task.Task;
import businesslogic.task.TaskEventReceiver;
import businesslogic.task.TaskSheet;
import businesslogic.turn.Turn;

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
}
