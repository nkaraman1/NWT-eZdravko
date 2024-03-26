package ba.unsa.etf.nwt.UserManagementService.repositories;

import ba.unsa.etf.nwt.UserManagementService.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findBynazivRole(String nazivRole);
}
