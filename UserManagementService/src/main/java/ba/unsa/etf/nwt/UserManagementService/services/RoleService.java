package ba.unsa.etf.nwt.UserManagementService.services;

import ba.unsa.etf.nwt.UserManagementService.exceptions.RoleErrorHandler;
import ba.unsa.etf.nwt.UserManagementService.exceptions.ErrorMsg;
import ba.unsa.etf.nwt.UserManagementService.model.Role;
import ba.unsa.etf.nwt.UserManagementService.model.User;
import ba.unsa.etf.nwt.UserManagementService.repositories.RoleRepository;
import ba.unsa.etf.nwt.UserManagementService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleErrorHandler roleErrorHandler;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.roleErrorHandler = new RoleErrorHandler();
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public ResponseEntity<?> getRoleByID(Long ID) {
        Optional<Role> role = roleRepository.findById(ID);
        if (role.isPresent()) {
            return new ResponseEntity<>(role.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorMsg(roleErrorHandler.getError(RoleErrorHandler.RoleErrorCode.ID_NOT_FOUND)), HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<?> getRoleByName(String nazivRole) {
        Optional<Role> role = roleRepository.findBynazivRole(nazivRole);

        if (role.isPresent()) {
            return new ResponseEntity<>(role, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorMsg(roleErrorHandler.getError(RoleErrorHandler.RoleErrorCode.ROLENAME_NOT_FOUND)), HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<?> createRole(String nazivRole, boolean potrebanKod) {
        Optional<Role> role = roleRepository.findBynazivRole(nazivRole);

        if (role.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg(roleErrorHandler.getError(RoleErrorHandler.RoleErrorCode.ROLE_ALREADY_EXISTS)), HttpStatus.FORBIDDEN);
        }
        else {
            try {
                Role newRole = new Role(nazivRole, potrebanKod);
                roleRepository.save(newRole);
                return new ResponseEntity<>(newRole, HttpStatus.OK);
            }
            catch (Exception e) {
                return new ResponseEntity<>(new ErrorMsg(e.getMessage()), HttpStatus.FORBIDDEN);
            }
        }
    }

    public ResponseEntity<?> deleteRole(Long ID) {
        Optional<Role> role = roleRepository.findById(ID);
        if (role.isPresent()) {
            List<User> useri = userRepository.findByRola(role.get());
            if (useri.isEmpty()) {
                roleRepository.deleteById(ID);
                return new ResponseEntity<>(role.get(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ErrorMsg(roleErrorHandler.getError(RoleErrorHandler.RoleErrorCode.USERS_EXIST_WITH_ROLE)), HttpStatus.FORBIDDEN);
            }
        }
        else {
            return new ResponseEntity<>(new ErrorMsg(roleErrorHandler.getError(RoleErrorHandler.RoleErrorCode.ID_NOT_FOUND)), HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<?> changeRoleCode(Long ID) {
        Optional<Role> role = roleRepository.findById(ID);
        if (role.isPresent()) {
            Role selected = role.get();
            selected.setKod(Role.generisiKod());
            var result = roleRepository.save(selected);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorMsg(roleErrorHandler.getError(RoleErrorHandler.RoleErrorCode.ID_NOT_FOUND)), HttpStatus.FORBIDDEN);
        }
    }
}
