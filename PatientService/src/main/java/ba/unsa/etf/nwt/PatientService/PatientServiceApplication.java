package ba.unsa.etf.nwt.PatientService;

import ba.unsa.etf.nwt.PatientService.model.*;
import ba.unsa.etf.nwt.PatientService.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {"ba.unsa.etf.nwt.PatientService"})
public class PatientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientServiceApplication.class, args);
	}






}
