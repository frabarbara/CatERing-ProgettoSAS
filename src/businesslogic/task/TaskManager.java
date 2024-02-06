package businesslogic.task;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import businesslogic.recipe.Recipe;
import businesslogic.turn.Turn;
import businesslogic.user.User;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class TaskManager {

    /*############################## PROPERTIES ##############################*/

    private EventInfo currentEvent;
    private TaskSheet currentTaskSheet;
    private ArrayList<TaskEventReceiver> eventReceivers;

    /*############################## CONSTRUCTORS ##############################*/

    public TaskManager() {
        super();
        this.eventReceivers = new ArrayList<>();
    }

    /*############################## GETTERS / SETTERS ##############################*/

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

    /*############################## METHODS ##############################*/

    public void openEvent(int eventID) {
        setCurrentEvent(CatERing.getInstance().getEventManager().getEvent(eventID));
    }

    public void openTaskSheet(ServiceInfo service) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if (!user.isChef()) {
            throw new UseCaseLogicException("[TaskManager: openTaskSheet(..)] ERROR: current user has to be a chef");
        }

        if (currentEvent.getAssignedChef().getId() != user.getId()) {
            throw new UseCaseLogicException("[TaskManager: openTaskSheet(..)] ERROR: current user is not the assigned chef for this event");
        }

        setCurrentTaskSheet(service.getTaskSheet());
    }

    public Task insertTask(Recipe job, int qty) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if (!user.isChef()) {
            throw new UseCaseLogicException("[TaskManager: openTaskSheet(..)] ERROR: current user has to be a chef");
        }

        if (currentEvent.getAssignedChef().getId() != user.getId()) {
            throw new UseCaseLogicException("[TaskManager: openTaskSheet(..)] ERROR: current user is not the assigned chef for this event");
        }

        Task newTask = currentTaskSheet.addTask(job, qty);

        notifyTaskAdded(currentTaskSheet, newTask);

        return newTask;
    }

    public void sortTask(Task t, int pos) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if (!user.isChef()) {
            throw new UseCaseLogicException("[TaskManager: openTaskSheet(..)] ERROR: current user has to be a chef");
        }

        if (currentEvent.getAssignedChef().getId() != user.getId()) {
            throw new UseCaseLogicException("[TaskManager: openTaskSheet(..)] ERROR: current user is not the assigned chef for this event");
        }

        try {
            this.currentTaskSheet.sortTask(t, pos);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        this.notifyTasksRearranged();

    }

    public ObservableList<Turn> getTurnTable() throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if (!user.isChef()) {
            throw new UseCaseLogicException("[TaskManager: openTaskSheet(..)] ERROR: current user has to be a chef");
        }

        return CatERing.getInstance().getTurnManager().getTurnTable();
    }

    public void scheduleTask(Task task, Turn turn) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if (!user.isChef()) {
            throw new UseCaseLogicException("[TaskManager: openTaskSheet(..)] ERROR: current user has to be a chef");
        }

        if (currentEvent.getAssignedChef().getId() != user.getId()) {
            throw new UseCaseLogicException("[TaskManager: openTaskSheet(..)] ERROR: current user is not the assigned chef for this event");
        }

        if (task.isScheduled()) {
            throw new UseCaseLogicException("[TaskManager: openTaskSheet(..)] ERROR: selected task is already scheduled");
        }

        try {
            CatERing.getInstance().getTurnManager().scheduleTask(task, turn);
            notifyTaskScheduled(task, turn);
        } catch (UseCaseLogicException e) {
            System.err.println(e.getMessage());
        }

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

    private void notifyTasksRearranged() {
        for (TaskEventReceiver er: eventReceivers) {
            er.updateTasksRearranged(this.currentTaskSheet);
        }
    }

    private void notifyTaskScheduled(Task task, Turn turn) {
        for (TaskEventReceiver er: eventReceivers) {
            er.updateTaskScheduled(task, turn);
        }
    }

}
