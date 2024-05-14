package ba.unsa.etf.nwt.APIGateway.GatewayFilter;

/*import ba.unsa.etf.nwt.systemevents.Event;
import ba.unsa.etf.nwt.systemevents.EventRequest;
import ba.unsa.etf.nwt.systemevents.EventResponse;
import ba.unsa.etf.nwt.systemevents.EventServiceGrpc;*/
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class CustomGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomGatewayFilterFactory.Config> {
    public CustomGatewayFilterFactory() {
        super(Config.class);
    }

    /*private static Event makeEvent(String method, String servis, int responseCode, String resurs) {
        Event event = new Event();

        // Privremeno dok se ne nađe način da se uzme userID preko JWT ili nekako
        event.setUser_id(1L);
        //

        event.setIme_servisa(servis);
        if (responseCode >= 200 && responseCode <= 299)
            event.setUspjesna_akcija(true);
        else
            event.setUspjesna_akcija(false);

        event.setResurs(resurs);
        event.setTimestamp(LocalDateTime.now().toString());

        if (method.equals("GET")) {
            event.setAkcija("GET");
        }
        else if (method.equals("POST")) {
            event.setAkcija("CREATE");
        }
        else if (method.equals("PUT") || method.equals("PATCH")) {
            event.setAkcija("UPDATE");
        }
        else {
            event.setAkcija("DELETE");
        }

        return event;
    }
     */

    /*private static EventRequest makeEventRequest(String method, String servis, int responseCode, String resurs) {
        EventRequest.Builder requestBuilder = EventRequest.newBuilder();
        EventRequest request = requestBuilder.build();

        // Privremeno dok se ne nađe način da se uzme userID preko JWT ili nekako
        requestBuilder.setUserId(1L);
        //

        requestBuilder.setImeServisa(servis);
        if (responseCode >= 200 && responseCode <= 299)
            requestBuilder.setUspjesnaAkcija(true);
        else
            requestBuilder.setUspjesnaAkcija(false);

        requestBuilder.setResurs(resurs);
        requestBuilder.setTimestamp(LocalDateTime.now().toString());

        if (method.equals("GET")) {
            requestBuilder.setAkcija("GET");
        }
        else if (method.equals("POST")) {
            requestBuilder.setAkcija("CREATE");
        }
        else if (method.equals("PUT") || method.equals("PATCH")) {
            requestBuilder.setAkcija("UPDATE");
        }
        else {
            requestBuilder.setAkcija("DELETE");
        }

        return request;
    }*/

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().toString();
            String method = exchange.getRequest().getMethod().toString();
            String headers = exchange.getRequest().getHeaders().toString();
            String service = config.getServiceName();

            // Log the information
            System.out.println("HTTP REQUEST:");
            System.out.println("Method: " + method);
            System.out.println("Path: " + path);
            System.out.println("Headers: " + headers);
            System.out.println("Service: " + service);

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                // Extracting response status and headers
                int status = exchange.getResponse().getStatusCode().value();
                HttpHeaders headersResponse = exchange.getResponse().getHeaders();

                // Log the outgoing response information
                System.out.println("HTTP RESPONSE:");
                System.out.println("Status: " + status);
                System.out.println("Headers: " + headersResponse);

                /*EventDTO eventDTO = makeEventDTO(method, service, status, path);
                System.out.println("EventDTO:");
                System.out.println(eventDTO);

                sendEventDTO(eventDTO);*/

                /*EventRequest request = makeEventRequest(method, service, status, path);
                System.out.println("Event:");
                System.out.println(request.getAkcija());
                System.out.println(request.getTimestamp());
                System.out.println(request.getImeServisa());
                System.out.println(request.getUspjesnaAkcija());
                sendRequest(request);*/
            }));
        };
    }

    /*private void sendRequest(EventRequest eventRequest) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9000)
                .usePlaintext()
                .build();

        // Create a gRPC stub
        EventServiceGrpc.EventServiceBlockingStub stub = EventServiceGrpc.newBlockingStub(channel);

        // Call the gRPC service method to send the EventDTO
        EventResponse response = stub.send(eventRequest);

        // Print response (if needed)
        System.out.println("gRPC Response:");
        System.out.println(response);

        // Shutdown the channel
        channel.shutdown();
    }*/

    public static class Config {
        private String serviceName;

        public Config(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }
    }
}
