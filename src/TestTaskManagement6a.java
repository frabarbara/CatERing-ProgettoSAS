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

public class TestTaskManagement6a {

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
            Turn targetTurn = Turn.loadTurnById(1);
            taskMgr.scheduleTask(taskToSchedule, targetTurn);

            System.out.println("task after scheduling:\n" + Task.loadTaskById(taskToScheduleId));

            // ASSIGN COOK TO TASK
            System.out.println("\nASSIGNING COOK");
            int taskId = 1;
            taskMgr.assignCook(Task.loadTaskById(taskId), new Cook(User.loadUserById(6)));

            System.out.println("cook " + Task.loadTaskById(taskId).getAssignedCook() + " assigned to task " + taskId);

            // CHANGE COOK FOR TASK
            System.out.println("\nCHANGING COOK");
            taskMgr.changeCook(Task.loadTaskById(taskId), new Cook(User.loadUserById(4)));

            System.out.println("cook " + Task.loadTaskById(taskId).getAssignedCook() + " assigned to task " + taskId);

            // RESET DB TO STARTING CONDITIONS
            TestUtils.resetDB();

        } catch (UseCaseLogicException e) {
            System.err.println(e.getMessage());
        }
    }

}
