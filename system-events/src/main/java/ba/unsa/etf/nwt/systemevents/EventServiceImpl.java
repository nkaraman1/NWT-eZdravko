package ba.unsa.etf.nwt.systemevents;

import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl extends EventServiceGrpc.EventServiceImplBase {
    private EventRepository eventRepository;
    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void send(EventRequest request, StreamObserver<EventResponse> responseObserver) {
        //super.send(request, responseObserver);
        Event event = new Event();
        event.setProperties(request);
        eventRepository.save(event);

        EventResponse response = EventResponse.newBuilder()
                .setMessage("Uspjesno dodan event!")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
