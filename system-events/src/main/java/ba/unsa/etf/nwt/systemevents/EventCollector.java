package ba.unsa.etf.nwt.systemevents;

import java.util.ArrayList;
import java.util.List;

public class EventCollector {
    private List<Event> events;

    private EventCollector() {
        this.events = new ArrayList<>();
    }

    private static class SingletonHelper {
        private static final EventCollector INSTANCE = new EventCollector();
    }

    public static EventCollector getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public List<Event> getEvents() {
        return events;
    }
}
