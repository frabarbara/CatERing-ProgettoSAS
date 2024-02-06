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
            taskMgr.openTaskSheet(taskMgr.getCurrentEvent().getServices().getFirst());
            taskMgr.getCurrentTaskSheet().setTasks(TaskSheet.loadTaskSheetInfoForService(taskMgr.getCurrentTaskSheet().getId()));
            System.out.println("opened task sheet: " + taskMgr.getCurrentTaskSheet() + "\n");

            // INSERT TASK
            /*Task newTask = taskMgr.insertTask(Recipe.loadRecipeById(17), 30);
            System.out.println("inserted task: " + newTask + "\n");*/

            // MOVE TASK
            /*System.out.println("SORTING TASKS\norder before:\n" + taskMgr.getCurrentTaskSheet());
            Task taskToMove = Task.loadTaskById(15);
            taskMgr.sortTask(taskToMove, 0);
            System.out.println("order after:\n" + taskMgr.getCurrentTaskSheet());*/

            // RETRIEVE TURN TABLE
            System.out.println("RETRIEVING TURN TABLE");
            ObservableList<Turn> turnTable = taskMgr.getTurnTable();
            for (Turn t: turnTable) {
                System.out.println(t);
            }

            // SCHEDULE TASK IN TURN
            /*
            System.out.println("SCHEDULING TASK");
            int taskToScheduleId = 1;
            Task taskToSchedule = Task.loadTaskById(taskToScheduleId);
            Turn targetTurn = Turn.loadTurnById(1);
            taskMgr.scheduleTask(taskToSchedule, targetTurn);

            System.out.println("task after scheduling: " + Task.loadTaskById(taskToScheduleId));
            */

            // ASSIGN COOK TO TASK
            System.out.println("ASSIGNING COOK");
            int taskId = 7;
            // taskMgr.assignCook(Task.loadTaskById(taskId), new Cook(User.loadUserById(5)));

            System.out.println("cook " + Task.loadTaskById(taskId).getAssignedCook() + " assigned to task " + taskId);

            // READ FEEDBACK
            System.out.println("READING FEEDBACK");
            System.out.println(taskMgr.readFeedback(Turn.loadTurnById(1)));

        } catch (UseCaseLogicException e) {
            System.out.println(e.getMessage());
        }
    }

}
