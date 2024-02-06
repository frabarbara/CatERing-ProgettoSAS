package businesslogic.task;

import businesslogic.recipe.Recipe;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TaskSheet {

    /*############################## PROPERTIES ##############################*/

    private int id;
    private ObservableList<Task> tasks;

    /*############################## CONSTRUCTORS ##############################*/

    public TaskSheet(int id) {
        super();
        this. id = id;
        this.tasks = FXCollections.observableArrayList();
    }

    /*############################## GETTERS / SETTERS ##############################*/

    public int getId() {
        return id;
    }

    public ObservableList<Task> getTasks() { return this.tasks; }

    public void setTasks(ObservableList<Task> tasks) {
        this.tasks = tasks;
    }

    /*############################## METHODS ##############################*/

    @Override
    public String toString() {
        String res = "TASK SHEET " + this.id + "\n";
        for (Task t: this.tasks) {
            res += (t + ";\n");
        }
        return res;
    }

    public Task addTask(Recipe job, int qty) {

        Task newTask = new Task(job, qty);
        tasks.add(newTask);

        return newTask;
    }

    public int getTaskPosition(Task t) {

        return this.tasks.indexOf(t);
    }

    public void sortTask(Task t, int pos) throws IllegalArgumentException {

        if (pos < 0 || pos >= this.tasks.size()) {
            throw new IllegalArgumentException("[TaskSheet: sortTask(...)] ERROR: illegal position");
        }

        this.tasks.remove(t);
        this.tasks.add(pos, t);
    }

    /*############################## PERSISTENCE METHODS ##############################*/

    public static ObservableList<Task> loadTaskSheetInfoForService(int serviceID) {

        // classe di supporto per poter caricare i Task nel TaskSheet secondo le posizioni riportate in DB

        class OrderedTask {
            private Task t;
            private int o;

            public OrderedTask(Task t, int o) {
                super();
                this.t = t;
                this.o = o;
            }

            public int getO() {
                return this.o;
            }

            public Task getT() {
                return this.t;
            }
        }

        ArrayList<OrderedTask> midResult = new ArrayList<>();
        ObservableList<Task> result = FXCollections.observableArrayList();
        String query = "SELECT * FROM tasks WHERE service_id = '" + serviceID + "'";

        PersistenceManager.executeQuery(query, rs -> {
            Recipe r = Recipe.loadRecipeById(rs.getInt("recipe_id"));
            int qty = rs.getInt("qty");
            Task newTask = new Task(r, qty);
            newTask.setId(rs.getInt("id"));
            newTask.setAssignedCook(User.loadUserById(rs.getInt("assigned_cook_id")));
            midResult.add(new OrderedTask(newTask, rs.getInt("position")));
        });

        for (int i = 0; i < midResult.size(); i++) {
            for (OrderedTask ot: midResult) {
                if (ot.getO() == i) {
                    result.add(ot.getT());
                }
            }
        }

        return result;
    }

    public static void saveTasksOrder(TaskSheet ts) {

        String upd = "UPDATE tasks SET position = ? WHERE id = ?";
        PersistenceManager.executeBatchUpdate(upd, ts.tasks.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, batchCount);
                ps.setInt(2, ts.tasks.get(batchCount).getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // no generated ids to handle
            }
        });
    }

}
