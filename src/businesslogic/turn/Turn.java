package businesslogic.turn;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Turn {

    /*############################## PROPERTIES ##############################*/

    private int id;
    private int capacity;
    private String feedback;

    /*############################## CONSTRUCTORS ##############################*/

    /*############################## GETTERS / SETTERS ##############################*/

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getFeedback() {
        return this.feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    /*############################## METHODS ##############################*/

    @Override
    public String toString() {
        return "TURN " + this.id + ";\tcapacity: " + this.capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != Turn.class) return false;

        Turn turnObject = (Turn) o;
        return this.getId() == turnObject.getId();
    }

    public boolean isFull() {
        String query = "SELECT COUNT(turn_id) as count FROM scheduled_tasks WHERE turn_id = " + this.id + ";";
        int[] result = new int[1];
        PersistenceManager.executeQuery(query, rs -> {
            result[0] = rs.getInt("count");
        });

        return result[0] >= this.capacity;
    }

    /*############################## PERSISTENCE METHODS ##############################*/

    public static Turn loadTurnById(int turnID) {
        String query = "SELECT * FROM turns WHERE id = " + turnID + ";";
        Turn newTurn = new Turn();
        PersistenceManager.executeQuery(query, rs -> {
            newTurn.setId(rs.getInt("id"));
            newTurn.setCapacity(rs.getInt("capacity"));
            newTurn.setFeedback(rs.getString("feedback"));
        });

        return newTurn;
    }

}
