import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.ServiceInfo;
import businesslogic.recipe.Recipe;
import businesslogic.task.Task;
import businesslogic.task.TaskManager;
import businesslogic.task.TaskSheet;
import businesslogic.turn.Turn;
import businesslogic.user.Cook;
import businesslogic.user.User;
import javafx.collections.ObservableList;

public class TestTaskManagement1a {

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

            // RESET TASK SHEET
            System.out.println("\nRESETTING TASK SHEET");
            System.out.println("task sheet before reset:");
            System.out.println(ServiceInfo.loadServiceById(6).getTaskSheet());
            taskMgr.resetTaskSheet(ServiceInfo.loadServiceById(6));
            System.out.println("task sheet after reset:");
            System.out.println(ServiceInfo.loadServiceById(6).getTaskSheet());

        } catch (UseCaseLogicException e) {
            System.out.println(e.getMessage());
        }
    }

}
