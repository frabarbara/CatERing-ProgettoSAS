import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.recipe.Recipe;
import businesslogic.task.Task;
import businesslogic.task.TaskManager;
import businesslogic.task.TaskSheet;
import businesslogic.turn.Turn;
import businesslogic.user.Cook;
import businesslogic.user.User;
import javafx.collections.ObservableList;

public class TestTaskManagement2a {

    public static void main(String[] args) {

        try {
            //LOGIN
            System.out.println("LOGGING IN");
            CatERing.getInstance().getUserManager().fakeLogin("Tony");
            System.out.println("logged in as " + CatERing.getInstance().getUserManager().getCurrentUser());

            //RETRIEVE TASK MANAGER
            TaskManager taskMgr = CatERing.getInstance().getTaskManager();

            //OPEN EVENT
            System.out.println("\nOPENING EVENT");
            taskMgr.openEvent(3);
            System.out.println("opened event: " + taskMgr.getCurrentEvent());

            //OPEN TASK SHEET
            System.out.println("\nOPENING TASK SHEET");
            taskMgr.openTaskSheet(taskMgr.getCurrentEvent().getServices().getFirst());
            taskMgr.getCurrentTaskSheet().setTasks(TaskSheet.loadTaskSheetInfoForService(taskMgr.getCurrentTaskSheet().getId()));
            System.out.println("opened task sheet: " + taskMgr.getCurrentTaskSheet());

            // MODIFY TASK QUANTITY
            System.out.println("\nMODIFYING TASK QUANTITY");
            System.out.println("task before change:");
            System.out.println(Task.loadTaskById(1));
            taskMgr.changeQty(Task.loadTaskById(1), 15);
            System.out.println("task after change:");
            System.out.println(Task.loadTaskById(1));

        } catch (UseCaseLogicException e) {
            System.err.println(e.getMessage());
        }
    }

}
