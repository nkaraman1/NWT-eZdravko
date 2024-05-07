package ba.unsa.etf.nwt.systemevents.repositories;

import ba.unsa.etf.nwt.systemevents.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
