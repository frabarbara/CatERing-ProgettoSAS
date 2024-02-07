package businesslogic.task;

import businesslogic.event.ServiceInfo;
import businesslogic.recipe.Recipe;
import businesslogic.turn.Turn;
import businesslogic.user.Cook;
import businesslogic.user.User;
import persistence.PersistenceManager;

public class Task {

    /*############################## PROPERTIES ##############################*/

    private int id;
    private Recipe job;
    private int avail;
    private int qty;
    private Cook assignedCook;
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

    public int getAvail() {
        return avail;
    }

    public void setAvail(int avail) {
        this.avail = avail;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Cook getAssignedCook() {
        return assignedCook;
    }

    public void setAssignedCook(Cook assignedCook) {
        this.assignedCook = assignedCook;
    }

    public Turn getAssignedTurn() { return this.assignedTurn; }

    public void setAssignedTurn(Turn assignedTurn) { this.assignedTurn = assignedTurn; }

    /*############################## METHODS ##############################*/

    @Override
    public String toString() {
        String res = "TASK " + this.id + ": " + this.qty + " x " + this.job.getName();
        if (this.assignedTurn != null) res += "\t - \tscheduled in turn " + this.assignedTurn.getId();
        if (this.assignedCook != null) res += " with cook " + this.assignedCook.getName();

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

    public void assignCook(Cook cook) {
        this.assignedCook = cook;
    }

    public void updateQty(int newQty) { this.qty = newQty; }

    public void updateAvail(int avail) {
        this.avail = avail;
        this.qty -= avail;
    }

    /*############################## PERSISTENCE METHODS ##############################*/

    public static Task loadTaskById(int taskID) {
        Task loadedTask = new Task();
        String query = "SELECT * FROM tasks LEFT JOIN scheduled_tasks ON tasks.id = scheduled_tasks.task_id WHERE tasks.id = " + taskID + ";";
        PersistenceManager.executeQuery(query, rs -> {
            loadedTask.setId(rs.getInt("id"));
            loadedTask.setJob(Recipe.loadRecipeById(rs.getInt("recipe_id")));
            loadedTask.setQty(rs.getInt("qty"));
            loadedTask.setAvail(rs.getInt("availability"));

            int assignedCookId = rs.getInt("assigned_cook_id");
            if (assignedCookId != 0)
                loadedTask.setAssignedCook(new Cook(User.loadUserById(assignedCookId)));

            int assignedTurnId = rs.getInt("turn_id");
            if (assignedTurnId != 0)
                loadedTask.setAssignedTurn(Turn.loadTurnById(assignedTurnId));
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

    public static void saveCookAssigned(Task task, Cook cook) {
        String query = "UPDATE tasks SET assigned_cook_id = " + cook.getId() + " WHERE id = " + task.getId() + ";";
        PersistenceManager.executeUpdate(query);
    }

    public static void eraseAllServiceTasks(ServiceInfo s) {
        String query = "DELETE FROM tasks WHERE service_id = " + s.getId() + ";";
        PersistenceManager.executeUpdate(query);
    }

    public static void updateNewQty(TaskSheet ts, Task t) {
        String query = "UPDATE tasks SET qty = " + t.getQty() + " WHERE id = " + t.getId() + ";";
        PersistenceManager.executeUpdate(query);
    }

    public static void updateNewAvail(TaskSheet ts, Task t) {
        String query = "UPDATE tasks SET availability = " + t.getAvail() + ", qty = " + t.getQty() + " WHERE id = " + t.getId() + ";";
        PersistenceManager.executeUpdate(query);
    }

    public static void eraseTask(Task t) {
        String query = "DELETE FROM tasks WHERE id = " + t.getId() + ";";
        PersistenceManager.executeUpdate(query);
    }

    public static void saveReschedule(Task task, Turn newTurn) {
        String query = "DELETE FROM scheduled_tasks WHERE task_id = " + task.getId() + ";";
        PersistenceManager.executeUpdate(query);
        query = "UPDATE tasks SET assigned_cook_id = NULL WHERE id = " + task.getId() + ";";
        PersistenceManager.executeUpdate(query);
        query = "INSERT INTO scheduled_tasks VALUES(" + task.getId() +", " + newTurn.getId() + ");";
        PersistenceManager.executeUpdate(query);
    }

    public static void updateRemoveTask(Task t) {
        String query = "UPDATE tasks SET assigned_cook_id = NULL WHERE id = " + t.getId() + ";";
        PersistenceManager.executeUpdate(query);
        query = "DELETE FROM scheduled_tasks WHERE task_id = " + t.getId() + ";";
        PersistenceManager.executeUpdate(query);
    }

    public static void updateChangeCook(Task t, Cook newCook) {
        String query = "UPDATE tasks SET assigned_cook_id = " + newCook.getId() + " WHERE id = " + t.getId() + ";";
        PersistenceManager.executeUpdate(query);
    }
}
