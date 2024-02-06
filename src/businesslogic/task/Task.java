package businesslogic.task;

import businesslogic.recipe.Recipe;
import businesslogic.user.User;

public class Task {

    private int id;
    private Recipe job;
    private int qty;
    private User assignedCook;

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
}
