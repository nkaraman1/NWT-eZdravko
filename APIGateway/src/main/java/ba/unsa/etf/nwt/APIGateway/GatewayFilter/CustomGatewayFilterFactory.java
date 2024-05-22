package ba.unsa.etf.nwt.APIGateway.GatewayFilter;

import ba.unsa.etf.nwt.systemevents.Event;
import ba.unsa.etf.nwt.systemevents.EventRequest;
import ba.unsa.etf.nwt.systemevents.EventResponse;
import ba.unsa.etf.nwt.systemevents.EventServiceGrpc;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
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

    private static EventRequest makeEventRequest(String method, String servis, int responseCode, String resurs) {
        EventRequest.Builder requestBuilder = EventRequest.newBuilder();

        // Privremeno dok se ne nađe način da se uzme userID preko JWT ili nekako
        requestBuilder.setUserId(1L);
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

        EventRequest request = requestBuilder.build();
        return request;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().toString();
            String method = exchange.getRequest().getMethod().toString();
            String headers = exchange.getRequest().getHeaders().toString();
            String service = config.getServiceName();

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                // Extracting response status and headers
                int status = exchange.getResponse().getStatusCode().value();
                HttpHeaders headersResponse = exchange.getResponse().getHeaders();

                EventRequest request = makeEventRequest(method, service, status, path);
                sendRequest(request);
            }));
        };
    }

    private void sendRequest(EventRequest eventRequest) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9000)
                .usePlaintext()
                .build();

        System.out.println("Event:");
        System.out.println(eventRequest.toString());

        //EventServiceGrpc.EventServiceBlockingStub stub = EventServiceGrpc.newBlockingStub(channel);
        EventServiceGrpc.EventServiceFutureStub stub = EventServiceGrpc.newFutureStub(channel);

        ListenableFuture<EventResponse> futureResponse = stub.send(eventRequest);

        Futures.addCallback(futureResponse, new FutureCallback<EventResponse>() {
            @Override
            public void onSuccess(EventResponse response) {
                // Print response (if needed)
                System.out.println("gRPC Response:");
                System.out.println(response);

                // Shutdown the channel
                channel.shutdown();
            }

            @Override
            public void onFailure(Throwable throwable) {
                // Handle failure
                throwable.printStackTrace();

                // Shutdown the channel
                channel.shutdown();
            }
        }, MoreExecutors.directExecutor());
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
