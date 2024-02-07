package businesslogic.task;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import businesslogic.recipe.Recipe;
import businesslogic.turn.Turn;
import businesslogic.user.Cook;
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

    private void checkUserIsAssignedChef(String methodName) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if (!user.isChef()) {
            throw new UseCaseLogicException("[TaskManager: " + methodName + "(..)] ERROR: current user has to be a chef");
        }

        if (currentEvent.getAssignedChef().getId() != user.getId()) {
            throw new UseCaseLogicException("[TaskManager: " + methodName + "(..)] ERROR: current user is not the assigned chef for this event");
        }
    }

    public void openEvent(int eventID) {
        setCurrentEvent(CatERing.getInstance().getEventManager().getEvent(eventID));
    }

    public void openTaskSheet(ServiceInfo service) throws UseCaseLogicException {
        checkUserIsAssignedChef("openTaskSheet");

        setCurrentTaskSheet(service.getTaskSheet());
    }

    public Task insertTask(Recipe job, int qty) throws UseCaseLogicException {
        checkUserIsAssignedChef("insertTask");

        Task newTask = currentTaskSheet.addTask(job, qty);

        notifyTaskAdded(currentTaskSheet, newTask);

        return newTask;
    }

    public void sortTask(Task t, int pos) throws UseCaseLogicException {
        checkUserIsAssignedChef("sortTask");

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
            throw new UseCaseLogicException("[TaskManager: getTurnTable(..)] ERROR: current user has to be a chef");
        }

        return CatERing.getInstance().getTurnManager().getTurnTable();
    }

    public void scheduleTask(Task task, Turn turn) throws UseCaseLogicException {
        checkUserIsAssignedChef("scheduleTask");

        if (task.isScheduled()) {
            throw new UseCaseLogicException("[TaskManager: scheduleTask(..)] ERROR: selected task is already scheduled");
        }

        try {
            CatERing.getInstance().getTurnManager().scheduleTask(task, turn);
            notifyTaskScheduled(task, turn);
        } catch (UseCaseLogicException e) {
            System.err.println(e.getMessage());
        }

    }

    public void assignCook(Task task, Cook cook) throws UseCaseLogicException {
        checkUserIsAssignedChef("assignCook");

        if (!task.isScheduled()) {
            throw new UseCaseLogicException("[TaskManager: assignCook(..)] ERROR: selected task is not scheduled");
        }

        CatERing.getInstance().getTurnManager().assignCook(task, cook);

        notifyCookAssigned(task, cook);
    }

    public String readFeedback(Turn t) throws UseCaseLogicException {
        checkUserIsAssignedChef("readFeedback");

        return CatERing.getInstance().getTurnManager().getFeedback(t);
    }

    public void resetTaskSheet(ServiceInfo s) throws UseCaseLogicException {
        checkUserIsAssignedChef("resetTaskSheet");

        TaskSheet ts = s.getTaskSheet();
        for (Task t: ts.getTasks()) {
            ts.deleteTask(t);
        }

        notifyTaskSheetReset(s);
    }

    public void changeQty(Task t, int newQty) throws UseCaseLogicException {
        checkUserIsAssignedChef("changeQty");

        if (currentTaskSheet == null) {
            throw new UseCaseLogicException("[TaskManager: changeQty(..)] ERROR: current task sheet is null");
        }

        if (t == null) {
            throw new UseCaseLogicException("[TaskManager: changeQty(..)] ERROR: selected task is null");
        }

        if (newQty < 1) {
            throw new UseCaseLogicException("[TaskManager: changeQty(..)] ERROR: quantity must be a positive amount");
        }

        Task modifiedTask = currentTaskSheet.updateQty(t, newQty);

        notifyQtyChanged(currentTaskSheet, modifiedTask);
    }

    public void changeAvail(Task task, int avail) throws UseCaseLogicException {
        checkUserIsAssignedChef("changeAvail");

        if (avail < 1 || avail > task.getQty()) {
            throw new UseCaseLogicException(("[TaskManager: changeAvail(..)] ERROR: illegal availability value"));
        }

        Task modifiedTask = currentTaskSheet.updateAvail(task, avail);

        notifyAvailChanged(currentTaskSheet, modifiedTask);
    }

    public void deleteTask(Task task) throws UseCaseLogicException {
        checkUserIsAssignedChef("deleteTask");

        currentTaskSheet.deleteTask(task);

        notifyTaskDeleted(currentTaskSheet, task);
    }

    public void rescheduleTask(Task task, Turn newTurn) throws UseCaseLogicException {
        checkUserIsAssignedChef("rescheduleTask");

        CatERing.getInstance().getTurnManager().rescheduleTask(task, newTurn);

        notifyTaskRescheduled(task, newTurn);
    }

    // removeTask removes the task from the turn it's scheduled in, deleteTask deletes the task completely
    public void removeTask(Task task) throws UseCaseLogicException {
        checkUserIsAssignedChef("removeTask");

        CatERing.getInstance().getTurnManager().removeTask(task);

        notifyTaskRemoved(task);
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

    private void notifyCookAssigned(Task task, Cook cook) {
        for (TaskEventReceiver er: eventReceivers) {
            er.updateCookAssigned(task, cook);
        }
    }

    private void notifyTaskSheetReset(ServiceInfo s) {
        for (TaskEventReceiver er: eventReceivers) {
            er.updateTaskSheetReset(s);
        }
    }

    private void notifyQtyChanged(TaskSheet ts, Task t) {
        for (TaskEventReceiver er: eventReceivers) {
            er.updateQtyChanged(ts, t);
        }
    }

    private void notifyAvailChanged(TaskSheet ts, Task t) {
        for (TaskEventReceiver er: eventReceivers) {
            er.updateAvailChanged(ts, t);
        }
    }

    private void notifyTaskDeleted(TaskSheet ts, Task t) {
        for (TaskEventReceiver er: eventReceivers) {
            er.updateTaskDeleted(ts, t);
        }
    }

    private void notifyTaskRescheduled(Task task, Turn newTurn) {
        for (TaskEventReceiver er: eventReceivers) {
            er.updateTaskRescheduled(task, newTurn);
        }
    }

    private void notifyTaskRemoved(Task t) {
        for (TaskEventReceiver er: eventReceivers) {
            er.updateTaskRemoved(t);
        }
    }

}
