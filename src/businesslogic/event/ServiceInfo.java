package businesslogic.event;

import businesslogic.task.Task;
import businesslogic.task.TaskSheet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public class ServiceInfo implements EventItemInfo {

    /*#################### PROPERTIES ####################*/

    private int id;
    private String name;
    private Date date;
    private Time timeStart;
    private Time timeEnd;
    private int participants;
    private TaskSheet taskSheet;

    /*#################### CONSTRUCTORS ####################*/

    public ServiceInfo(String name) {
        this.name = name;
    }

    /*#################### GETTERS / SETTERS ####################*/

    public int getId() { return this.id; }

    public TaskSheet getTaskSheet() {
        return taskSheet;
    }

    /*#################### METHODS ####################*/

    public String toString() {
        return name + ": " + date + " (" + timeStart + "-" + timeEnd + "), " + participants + " pp.";
    }

    // STATIC METHODS FOR PERSISTENCE

    public static ObservableList<ServiceInfo> loadServiceInfoForEvent(int event_id) {
        ObservableList<ServiceInfo> result = FXCollections.observableArrayList();
        String query = "SELECT id, name, service_date, time_start, time_end, expected_participants " +
                "FROM Services WHERE event_id = " + event_id;
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                String s = rs.getString("name");
                ServiceInfo serv = new ServiceInfo(s);
                serv.id = rs.getInt("id");
                serv.date = rs.getDate("service_date");
                serv.timeStart = rs.getTime("time_start");
                serv.timeEnd = rs.getTime("time_end");
                serv.participants = rs.getInt("expected_participants");
                serv.taskSheet = new TaskSheet(serv.id);
                serv.taskSheet.setTasks(TaskSheet.loadTaskSheetInfoForService(serv.id));
                result.add(serv);
            }
        });

        return result;
    }

    public static ServiceInfo loadServiceById(int serviceID) {
        ServiceInfo[] result = new ServiceInfo[1];
        String query = "SELECT * FROM services WHERE id = " + serviceID + ";";
        PersistenceManager.executeQuery(query, rs -> {
            String s = rs.getString("name");
            result[0] = new ServiceInfo(s);
            result[0].id = serviceID;
            result[0].date = rs.getDate("service_date");
            result[0].timeStart = rs.getTime("time_start");
            result[0].timeEnd = rs.getTime("time_end");
            result[0].participants = rs.getInt("expected_participants");
            result[0].taskSheet = new TaskSheet(serviceID);
            result[0].taskSheet.setTasks(TaskSheet.loadTaskSheetInfoForService(serviceID));
        });

        return result[0];
    }
}
