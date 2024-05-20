package ba.unsa.etf.nwt.systemevents;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.PreDestroy;

@SpringBootApplication
public class SystemEventsApplication {
	@Autowired
	private EventRepository eventRepository;
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SystemEventsApplication.class, args);
	}

	@PreDestroy
	public void test() {
		EventCollector eventCollector = EventCollector.getInstance();
		System.out.println(eventCollector.getEvents());
		eventRepository.saveAll(eventCollector.getEvents());
		System.out.println(eventRepository.findAll());
	}
	@PostConstruct
	public void nesto() {
		EventCollector eventCollector = EventCollector.getInstance();
		System.out.println(eventCollector.getEvents());
		System.out.println(eventRepository.findAll());
	}
}
