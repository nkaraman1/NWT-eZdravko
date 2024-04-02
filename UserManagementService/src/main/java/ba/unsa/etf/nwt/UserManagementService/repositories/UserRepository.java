package ba.unsa.etf.nwt.UserManagementService.repositories;

import ba.unsa.etf.nwt.UserManagementService.model.Role;
import ba.unsa.etf.nwt.UserManagementService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByImeAndPrezime(String ime, String prezime);
    List<User> findByIme(String ime);
    List<User> findByPrezime(String prezime);
    List<User> findByEmail(String email);
    List<User> findByEmailAndPassword(String email, String password);
    List<User> findByRola(Role rola);
}