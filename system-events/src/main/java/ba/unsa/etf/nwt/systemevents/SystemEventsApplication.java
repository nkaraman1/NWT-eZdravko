package ba.unsa.etf.nwt.systemevents;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SystemEventsApplication {
	@Autowired
	private EventServiceImpl eventService;

	private Server grpcServer;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SystemEventsApplication.class, args);
	}
	@PostConstruct
	public void startGrpcServer() throws Exception {
		grpcServer = ServerBuilder.forPort(9000)
				.addService(eventService)
				.build();
		grpcServer.start();
		System.out.println("gRPC server started, listening on port 9000");
		grpcServer.awaitTermination();
	}
}
