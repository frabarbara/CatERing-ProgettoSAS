package businesslogic.task;

import businesslogic.recipe.Recipe;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskSheet {

    private int id;
    private ObservableList<Task> tasks;

    public int getId() {
        return id;
    }

    public void setTasks(ObservableList<Task> tasks) {
        this.tasks = tasks;
    }

    public TaskSheet(int id) {
        super();
        this. id = id;
        this.tasks = FXCollections.observableArrayList();
    }

    public Task addTask(Recipe job, int qty) {

        Task newTask = new Task(job, qty);
        tasks.add(newTask);

        return newTask;
    }

    /*############################## PERSISTENCE METHODS ##############################*/

    public static ObservableList<Task> loadTaskSheetInfoForService(int serviceID) {
        ObservableList<Task> result = FXCollections.observableArrayList();
        String query = "SELECT * FROM tasks WHERE service_id = '" + serviceID + "'";

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                Recipe r = Recipe.loadRecipeById(rs.getInt("recipe_id"));
                int qty = rs.getInt("qty");
                Task newTask = new Task(r, qty);
                newTask.setId(rs.getInt("id"));
                newTask.setAssignedCook(User.loadUserById(rs.getInt("assigned_cook_id")));
                result.add(newTask);
            }
        });

        return result;
    }

    public static void saveNewTask(int service_id, Task t) {

        String query = "INSERT INTO catering.tasks (service_id, recipe_id, qty) VALUES(" + service_id + ", " + t.getJob().getId() + ", " + t.getQty() + ");";
        PersistenceManager.executeUpdate(query);
        t.setId(PersistenceManager.getLastId());

    }

}
