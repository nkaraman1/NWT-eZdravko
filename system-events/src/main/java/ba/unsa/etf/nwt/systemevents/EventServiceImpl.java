package ba.unsa.etf.nwt.systemevents;

import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl extends EventServiceGrpc.EventServiceImplBase {

    @Override
    public void send(EventRequest request, StreamObserver<EventResponse> responseObserver) {
        Event event = new Event();
        event.setProperties(request);

        //eventDAO.save(event);
        //System.out.println(eventDAO.getEvents());
        //eventRepository.save(event);

        EventCollector eventCollector = EventCollector.getInstance();
        eventCollector.addEvent(event);

        System.out.println(event.toString());
        System.out.println(request.toString());
        EventResponse response = EventResponse.newBuilder()
                .setMessage("Uspjesno dodan event!")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}
