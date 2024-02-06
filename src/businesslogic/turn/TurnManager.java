package businesslogic.turn;

import businesslogic.UseCaseLogicException;
import businesslogic.task.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;

public class TurnManager {

    /*############################## PROPERTIES ##############################*/

    private ObservableList<Turn> turnTable;

    /*############################## CONSTRUCTORS ##############################*/

    public TurnManager() {
        super();
        this.turnTable = TurnManager.loadAllTurns();
    }

    /*############################## METHODS ##############################*/

    public ObservableList<Turn> getTurnTable() {
        return this.turnTable;
    }

    public void scheduleTask(Task task, Turn turn) throws UseCaseLogicException {
        int turnIdx = this.turnTable.indexOf(turn);
        System.out.println(turnIdx);
        Turn tableTurn = this.turnTable.get(turnIdx);

        if (tableTurn.isFull()) {
            throw new UseCaseLogicException("[TurnManager: scheduleTask(...)] ERROR: turn is already full");
        }

        task.setAssignedTurn(tableTurn);
    }

    /*############################## PERSISTENCE METHODS ##############################*/

    public static ObservableList<Turn> loadAllTurns() {
        String query = "SELECT * FROM turns";
        ObservableList<Turn> result = FXCollections.observableArrayList();
        PersistenceManager.executeQuery(query, rs -> {
            result.add(Turn.loadTurnById(rs.getInt("id")));
        });

        return result;
    }

}
