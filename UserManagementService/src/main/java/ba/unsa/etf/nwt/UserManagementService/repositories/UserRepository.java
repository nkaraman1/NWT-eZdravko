package ba.unsa.etf.nwt.UserManagementService.repositories;

import ba.unsa.etf.nwt.UserManagementService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

}