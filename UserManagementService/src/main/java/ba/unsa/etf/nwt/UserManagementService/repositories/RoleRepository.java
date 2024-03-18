package ba.unsa.etf.nwt.UserManagementService.repositories;

import ba.unsa.etf.nwt.UserManagementService.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
