import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.recipe.Recipe;
import businesslogic.task.Task;
import businesslogic.task.TaskManager;
import businesslogic.task.TaskSheet;
import businesslogic.turn.Turn;
import javafx.collections.ObservableList;

public class TestTaskManagement5b {

    public static void main(String[] args) {

        try {
            // LOGIN
            System.out.println("LOGGING IN");
            CatERing.getInstance().getUserManager().fakeLogin("Tony");
            System.out.println("logged in as " + CatERing.getInstance().getUserManager().getCurrentUser());

            // RETRIEVE TASK MANAGER
            TaskManager taskMgr = CatERing.getInstance().getTaskManager();

            // OPEN EVENT
            System.out.println("\nOPENING EVENT");
            taskMgr.openEvent(3);
            System.out.println("opened event: " + taskMgr.getCurrentEvent());

            // OPEN TASK SHEET
            System.out.println("\nOPENING TASK SHEET");
            taskMgr.openTaskSheet(taskMgr.getCurrentEvent().getServices().getFirst());
            taskMgr.getCurrentTaskSheet().setTasks(TaskSheet.loadTaskSheetInfoForService(taskMgr.getCurrentTaskSheet().getId()));
            System.out.println("opened task sheet: " + taskMgr.getCurrentTaskSheet());

            // INSERT TASK
            System.out.println("\nINSERTING TASK");
            Task newTask = taskMgr.insertTask(Recipe.loadRecipeById(17), 30);
            System.out.println("inserted task: " + newTask + "\n");

            // MOVE TASK
            System.out.println("SORTING TASKS\norder before:\n" + taskMgr.getCurrentTaskSheet());
            Task taskToMove = Task.loadTaskById(2);
            taskMgr.sortTask(taskToMove, 0);
            System.out.println("order after:\n" + taskMgr.getCurrentTaskSheet());


            // RETRIEVE TURN TABLE
            System.out.println("RETRIEVING TURN TABLE");
            ObservableList<Turn> turnTable = taskMgr.getTurnTable();
            for (Turn t: turnTable) {
                System.out.println(t);
            }

            // SCHEDULE TASK IN TURN
            System.out.println("\nSCHEDULING TASK");
            int taskToScheduleId = 1;
            Task taskToSchedule = Task.loadTaskById(taskToScheduleId);
            System.out.println("task before scheduling:\n" + taskToSchedule);
            Turn targetTurn = Turn.loadTurnById(1);
            taskMgr.scheduleTask(taskToSchedule, targetTurn);
            System.out.println("task after scheduling:\n" + Task.loadTaskById(taskToScheduleId));

            // REMOVE TASK FROM TURN
            System.out.println("\nREMOVING TASK FROM TURN");
            int taskToRemoveId = 1;
            Task taskToRemove = Task.loadTaskById(taskToRemoveId);
            System.out.println("task before removing:\n" + taskToRemove);
            taskMgr.removeTask(taskToRemove);

            System.out.println("task after removing:\n" + Task.loadTaskById(taskToRemoveId));

        } catch (UseCaseLogicException e) {
            System.err.println(e.getMessage());
        }
    }

}
