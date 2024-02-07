import persistence.PersistenceManager;

import java.io.IOException;

public class TestUtils {

    public static void resetDB() {
        // RESET DB TO STARTING CONDITIONS
        System.out.println("\nPRESS ENTER TO RESET DB TO STARTING CONDITIONS ...");
        try {
            System.in.read();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        PersistenceManager.resetDB();
    }

}
