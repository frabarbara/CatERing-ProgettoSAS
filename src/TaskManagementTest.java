import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.EventInfo;
import businesslogic.recipe.Recipe;
import businesslogic.task.TaskManager;
import businesslogic.task.TaskSheet;

public class TaskManagementTest {

    public static void main(String[] args) {

        try {
            //LOGIN
            CatERing.getInstance().getUserManager().fakeLogin("Tony");
            System.out.println("logged in as " + CatERing.getInstance().getUserManager().getCurrentUser() + "\n");

            //RETRIEVE TASK MANAGER
            TaskManager taskMgr = CatERing.getInstance().getTaskManager();

            //OPEN EVENT
            taskMgr.openEvent(3);
            System.out.println("opened event: " + taskMgr.getCurrentEvent() + "\n");

            //OPEN TASK SHEET
            TaskSheet taskSheet = taskMgr.openTaskSheet(taskMgr.getCurrentEvent().getServices().getFirst());
            System.out.println("opened task sheet: " + taskSheet + "\n");

            //INSERT TASK
            taskMgr.insertTask(Recipe.loadRecipeById(5), 10);

        } catch (UseCaseLogicException e) {
            System.out.println(e.getMessage());
        }
    }

}
