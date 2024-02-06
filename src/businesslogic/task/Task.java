package businesslogic.task;

import businesslogic.recipe.Recipe;
import businesslogic.user.User;
import persistence.PersistenceManager;

public class Task {

    private int id;
    private Recipe job;
    private int qty;
    private User assignedCook;

    public Task() {
        super();
    }

    public Task(Recipe j, int q) {
        super();
        this.job = j;
        this.qty = q;
    }

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

    @Override
    public String toString() {
        return "TASK " + this.id + ": " + this.qty + " x " + this.job.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != Task.class) {
            return false;
        }

        Task otherTask = (Task)o;
        return this.id == otherTask.getId();
    }

    public static Task loadTaskById(int taskID) {
        Task loadedTask = new Task();
        String query = "SELECT * FROM tasks WHERE id = " + taskID + ";";
        PersistenceManager.executeQuery(query, rs -> {
            loadedTask.setId(rs.getInt("id"));
            loadedTask.setJob(Recipe.loadRecipeById(rs.getInt("recipe_id")));
            loadedTask.setQty(rs.getInt("qty"));
            loadedTask.setAssignedCook(User.loadUserById(rs.getInt("assigned_cook_id")));
        });

        return loadedTask;
    }

    public static void saveNewTask(int service_id, Task t, int pos) {

        String query = "INSERT INTO catering.tasks (service_id, recipe_id, qty, position) VALUES(" + service_id + ", " + t.getJob().getId() + ", " + t.getQty() + ", " + pos + ");";
        PersistenceManager.executeUpdate(query);
        t.setId(PersistenceManager.getLastId());
    }
}
