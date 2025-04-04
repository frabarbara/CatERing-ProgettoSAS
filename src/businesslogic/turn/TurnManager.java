package businesslogic.turn;

import businesslogic.UseCaseLogicException;
import businesslogic.task.Task;
import businesslogic.user.Cook;
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
        Turn tableTurn = this.turnTable.get(turnIdx);

        if (tableTurn.isFull()) {
            throw new UseCaseLogicException("[TurnManager: scheduleTask(...)] ERROR: turn is already full");
        }

        task.setAssignedTurn(tableTurn);
    }

    public void assignCook(Task task, Cook cook) throws UseCaseLogicException {
        Turn turn = task.getAssignedTurn();

        if (!cook.isAvailable(turn)) {
            throw new UseCaseLogicException("[TurnManager: assignCook(...)] ERROR: cook is not available in the specified turn");
        }

        task.assignCook(cook);
    }

    public String getFeedback(Turn t) {
        return t.getFeedback();
    }

    public void rescheduleTask(Task task, Turn newTurn) throws UseCaseLogicException {
        int idx = turnTable.indexOf(newTurn);
        Turn listedTurn = turnTable.get(idx);

        if (listedTurn.isFull()) {
            throw new UseCaseLogicException("[TurnManager: rescheduleTask(..)] ERROR: turn selected is already full");
        }

        task.setAssignedTurn(listedTurn);
    }

    public void removeTask(Task t) {
        t.setAssignedTurn(null);
    }

    public void changeCook(Task t, Cook newCook) throws UseCaseLogicException {
        Turn turn = t.getAssignedTurn();
        if (!newCook.isAvailable(turn)) throw new UseCaseLogicException("[TurnManager: changeCook(..)] ERROR: selected cook is not available in task's turn");

        t.assignCook(newCook);
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
