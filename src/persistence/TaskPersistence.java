package persistence;

import businesslogic.task.Task;
import businesslogic.task.TaskEventReceiver;
import businesslogic.task.TaskSheet;

public class TaskPersistence implements TaskEventReceiver {

    @Override
    public void updateTaskAdded(TaskSheet ts, Task t) {
        TaskSheet.saveNewTask(ts.getId(), t);
    }
}
