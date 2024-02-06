package businesslogic.task;

import businesslogic.recipe.Recipe;
import businesslogic.turn.Turn;
import businesslogic.user.User;
import persistence.PersistenceManager;

public class Task {

    /*############################## PROPERTIES ##############################*/

    private int id;
    private Recipe job;
    private int qty;
    private User assignedCook;
    private Turn assignedTurn;

    /*############################## CONSTRUCTORS ##############################*/

    public Task() {
        super();
    }

    public Task(Recipe j, int q) {
        super();
        this.job = j;
        this.qty = q;
    }

    /*############################## GETTERS / SETTERS ##############################*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Recipe getJob() {
        return job;
    }

    public void setJob(Recipe job) {
        this.job = job;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public User getAssignedCook() {
        return assignedCook;
    }

    public void setAssignedCook(User assignedCook) {
        this.assignedCook = assignedCook;
    }

    private Turn getAssignedTurn() { return this.assignedTurn; }

    public void setAssignedTurn(Turn assignedTurn) { this.assignedTurn = assignedTurn; }

    /*############################## METHODS ##############################*/

    @Override
    public String toString() {
        String res = "TASK " + this.id + ": " + this.qty + " x " + this.job.getName();
        if (this.assignedTurn != null) res += ", scheduled in turn " + this.getAssignedTurn().getId();

        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != Task.class) {
            return false;
        }

        Task otherTask = (Task)o;
        return this.id == otherTask.getId();
    }

    public boolean isScheduled() {
        return this.assignedTurn != null;
    }

    /*############################## PERSISTENCE METHODS ##############################*/

    public static Task loadTaskById(int taskID) {
        Task loadedTask = new Task();
        String query = "SELECT * FROM tasks LEFT JOIN scheduled_tasks ON tasks.id = scheduled_tasks.task_id WHERE tasks.id = " + taskID + ";";
        PersistenceManager.executeQuery(query, rs -> {
            loadedTask.setId(rs.getInt("id"));
            loadedTask.setJob(Recipe.loadRecipeById(rs.getInt("recipe_id")));
            loadedTask.setQty(rs.getInt("qty"));
            loadedTask.setAssignedCook(User.loadUserById(rs.getInt("assigned_cook_id")));
            int assignedTurnId = rs.getInt("turn_id");
            if (assignedTurnId != 0) {
                loadedTask.setAssignedTurn(Turn.loadTurnById(assignedTurnId));
            }
        });

        return loadedTask;
    }

    public static void saveNewTask(int service_id, Task t, int pos) {

        String query = "INSERT INTO catering.tasks (service_id, recipe_id, qty, position) VALUES(" + service_id + ", " + t.getJob().getId() + ", " + t.getQty() + ", " + pos + ");";
        PersistenceManager.executeUpdate(query);
        t.setId(PersistenceManager.getLastId());
    }

    public static void saveSchedule(Task task, Turn turn) {

        String query = "INSERT INTO scheduled_tasks (task_id, turn_id) VALUES(" + task.getId() + ", " + turn.getId() + ");";
        PersistenceManager.executeUpdate(query);
    }
}
