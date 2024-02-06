package businesslogic.event;

import javafx.collections.ObservableList;

public class EventManager {
    public ObservableList<EventInfo> getEventInfo() {
        return EventInfo.loadAllEventInfo();
    }
    public EventInfo getEvent(int eventID) { return EventInfo.loadEventInfo(eventID); }
}
