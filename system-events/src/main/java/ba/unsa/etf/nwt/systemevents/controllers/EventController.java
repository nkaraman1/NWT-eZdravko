package ba.unsa.etf.nwt.systemevents.controllers;

import ba.unsa.etf.nwt.systemevents.DTO.EventDTO;
import ba.unsa.etf.nwt.systemevents.model.Event;
import ba.unsa.etf.nwt.systemevents.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping(value="/events")
    public List<Event> getEvents() { return eventService.getAllEvents(); }

    @PostMapping(value="/events/create")
    public ResponseEntity<?> createEvent(@RequestBody EventDTO eventDTO) {
        return eventService.createEvent(eventDTO);
    }
}
