package businesslogic.task;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import businesslogic.recipe.Recipe;
import businesslogic.user.User;

import java.util.ArrayList;

public class TaskManager {

    private EventInfo currentEvent;
    private TaskSheet currentTaskSheet;
    private ArrayList<TaskEventReceiver> eventReceivers;

    public TaskManager() {
        super();
        this.eventReceivers = new ArrayList<>();
    }

    public EventInfo getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(EventInfo currentEvent) {
        this.currentEvent = currentEvent;
    }

    public TaskSheet getCurrentTaskSheet() {
        return currentTaskSheet;
    }

    public void setCurrentTaskSheet(TaskSheet currentTaskSheet) {
        this.currentTaskSheet = currentTaskSheet;
    }

    public void openEvent(int eventID) {
        setCurrentEvent(CatERing.getInstance().getEventManager().getEvent(eventID));
    }

    public TaskSheet openTaskSheet(ServiceInfo service) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if (!user.isChef()) {
            throw new UseCaseLogicException("[TaskManager: openTaskSheet(..)] ERROR: current user has to be a chef");
        }

        if (currentEvent.getAssignedChef().getId() != user.getId()) {
            throw new UseCaseLogicException("[TaskManager: openTaskSheet(..)] ERROR: current user does not ");
        }

        setCurrentTaskSheet(service.getTaskSheet());

        return currentTaskSheet;
    }

    public void insertTask(Recipe job, int qty) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if (!user.isChef()) {
            throw new UseCaseLogicException("[TaskManager: openTaskSheet(..)] ERROR: current user has to be a chef");
        }

        if (currentEvent.getAssignedChef().getId() != user.getId()) {
            throw new UseCaseLogicException("[TaskManager: openTaskSheet(..)] ERROR: current user does not ");
        }

        Task newTask = currentTaskSheet.addTask(job, qty);

        notifyTaskAdded(currentTaskSheet, newTask);
    }


    /*############################## EVENT EMITTER METHODS ##############################*/

    public void addEventReceiver(TaskEventReceiver rec) {
        this.eventReceivers.add(rec);
    }

    public void removeEventReceiver(TaskEventReceiver rec) {
        this.eventReceivers.remove(rec);
    }

    private void notifyTaskAdded(TaskSheet ts, Task t) {
        for (TaskEventReceiver er: eventReceivers) {
            er.updateTaskAdded(ts, t);
        }
    }

}
