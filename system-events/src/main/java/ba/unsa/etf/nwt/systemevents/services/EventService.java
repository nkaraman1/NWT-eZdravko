package ba.unsa.etf.nwt.systemevents.services;

import ba.unsa.etf.nwt.systemevents.DTO.EventDTO;
import ba.unsa.etf.nwt.systemevents.model.Event;
import ba.unsa.etf.nwt.systemevents.repositories.EventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() { return eventRepository.findAll(); }

    public ResponseEntity<?> createEvent(EventDTO eventDTO) {
        try {
            Event event = convertToEntity(eventDTO);
            eventRepository.save(event);
            return new ResponseEntity<>(event, HttpStatus.OK);
        }
        catch(Exception e) {
            return new ResponseEntity<>("Unable to create event", HttpStatus.FORBIDDEN);
        }
    }

    private Event convertToEntity(EventDTO eventDTO) {
        Event event = new Event();
        event.setAkcija(eventDTO.getAkcija());
        event.setResurs(eventDTO.getResurs());
        event.setTimestamp(eventDTO.getTimestamp());
        event.setImeServisa(eventDTO.getImeServisa());
        event.setUserID(eventDTO.getUserID());
        event.setUspjesnaAkcija(eventDTO.getUspjesnaAkcija());
        return event;
    }
}
