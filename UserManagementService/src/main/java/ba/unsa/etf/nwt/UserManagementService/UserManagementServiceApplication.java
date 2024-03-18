package ba.unsa.etf.nwt.UserManagementService;

import ba.unsa.etf.nwt.UserManagementService.model.Role;
import ba.unsa.etf.nwt.UserManagementService.model.User;
import ba.unsa.etf.nwt.UserManagementService.repositories.RoleRepository;
import ba.unsa.etf.nwt.UserManagementService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class UserManagementServiceApplication {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	public static void main(String[] args) throws Exception {

		SpringApplication.run(UserManagementServiceApplication.class, args);
	}

	@GetMapping(value="/users")
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@GetMapping(value="/roles")
	public List<Role> getRoles() {
		return roleRepository.findAll();
	}
}
