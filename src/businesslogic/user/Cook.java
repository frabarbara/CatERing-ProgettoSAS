package businesslogic.user;

import businesslogic.turn.Turn;
import persistence.PersistenceManager;

public class Cook {

    /*############################## PROPERTIES ##############################*/

    private User user;

    /*############################## CONSTRUCTORS ##############################*/

    public Cook(User u) {
        super();
        this.user = u;
    }

    /*############################## METHODS ##############################*/

    @Override
    public String toString() {
        return "COOK: " + this.getName();
    }

    public int getId() {
        return this.user.getId();
    }

    public String getName() {return this.user.getUserName(); }

    public boolean isAvailable(Turn turn) {
        String query = "SELECT COUNT(*) AS count FROM cooks_availabilities WHERE cook_id = " + this.user.getId() + " AND turn_id = " + turn.getId() + ";";
        int[] result = new int[1];
        PersistenceManager.executeQuery(query, rs -> {
            result[0] = rs.getInt("count");
        });

        return result[0] == 1;
    }

    /*############################## PERSISTENCE METHODS ##############################*/

    public static void removeAvailability(Turn t, Cook c) {
        String query = "DELETE FROM cooks_availabilities WHERE cook_id = " + c.getId() + " AND turn_id = " + t.getId() + ";";
        PersistenceManager.executeUpdate(query);
    }

}
