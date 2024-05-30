package ba.unsa.etf.nwt.ForumService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {"ba.unsa.etf.nwt.ForumService"})
public class ForumServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ForumServiceApplication.class, args);
	}
}
