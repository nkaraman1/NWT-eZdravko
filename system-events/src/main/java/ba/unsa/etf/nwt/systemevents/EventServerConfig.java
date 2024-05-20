package ba.unsa.etf.nwt.systemevents;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
public class EventServerConfig {
    @Autowired
    private EventServiceImpl eventService;
    private Server server;

    @PostConstruct
    public void startGrpcServer() throws IOException, InterruptedException {
        System.out.println("gRPC server started, listening on port 9000");
        server = ServerBuilder.forPort(9000)
                .addService(eventService)
                .build()
                .start();

        server.awaitTermination();
    }

    @PreDestroy
    public void stopGrpcServer() {
        if (server != null)
            server.shutdown();
    }
}
