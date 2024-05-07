package ba.unsa.etf.nwt.APIGateway.GatewayFilter;

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

    private static EventDTO makeEventDTO(String method, String servis, int responseCode, String resurs) {
        EventDTO eventDTO = new EventDTO();

        // Privremeno dok se ne nađe način da se uzme userID preko JWT ili nekako
        eventDTO.setUserID(1L);
        //

        eventDTO.setImeServisa(servis);
        if (responseCode >= 200 && responseCode <= 299)
            eventDTO.setUspjesnaAkcija(true);
        else
            eventDTO.setUspjesnaAkcija(false);

        eventDTO.setResurs(resurs);
        eventDTO.setTimestamp(LocalDateTime.now());

        if (method.equals("GET")) {
            eventDTO.setAkcija("GET");
        }
        else if (method.equals("POST")) {
            eventDTO.setAkcija("CREATE");
        }
        else if (method.equals("PUT") || method.equals("PATCH")) {
            eventDTO.setAkcija("UPDATE");
        }
        else {
            eventDTO.setAkcija("DELETE");
        }

        return eventDTO;
    }
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

                EventDTO eventDTO = makeEventDTO(method, service, status, path);
                System.out.println("EventDTO:");
                System.out.println(eventDTO);
            }));
        };
    }

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
