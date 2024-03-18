package ba.unsa.etf.nwt.UserManagementService;

import ba.unsa.etf.nwt.UserManagementService.model.Role;
import ba.unsa.etf.nwt.UserManagementService.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@SpringBootApplication
@RestController
public class UserManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserManagementServiceApplication.class, args);
	}

	@GetMapping
	public List<User> test() {
		return List.of(
				new User("Elvedin",
						"Smajic",
						LocalDate.of(2001, Month.JANUARY, 10),
						0,
						"123456789",
						"esmajic2@etf.unsa.ba",
						"sifra123",
						"Adresa 123",
						"img_path",
						new Role("Doktor"),
						"UID-123",
						"123456"),

				new User("Neko",
						"Nekic",
						LocalDate.of(1980, Month.JUNE, 1),
						0,
						"123456789",
						"nnekic1@etf.unsa.ba",
						"sifra123",
						"Adresa 123",
						"img_path",
						new Role("Pacijent"),
						"UID-123",
						"123456")
		);
	}
}
