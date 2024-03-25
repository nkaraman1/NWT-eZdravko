package ba.unsa.etf.nwt.UserManagementService.controllers;

import ba.unsa.etf.nwt.UserManagementService.model.ErrorMsg;
import ba.unsa.etf.nwt.UserManagementService.model.Role;
import ba.unsa.etf.nwt.UserManagementService.model.User;
import ba.unsa.etf.nwt.UserManagementService.repositories.RoleRepository;
import ba.unsa.etf.nwt.UserManagementService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class RoleController {
    private final RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @GetMapping(value="/roles")
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
    @GetMapping(value = "/roles/id/{id}")
    public ResponseEntity<?> getRoleByID(@PathVariable("id") Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            return new ResponseEntity<>(role.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorMsg("Ne postoji rola sa tim ID-om!"), HttpStatus.FORBIDDEN);
        }
    }
    @GetMapping(value = "/roles/name/{nazivRole}")
    public ResponseEntity<?> getRoleByName(@PathVariable String nazivRole) {
        Optional<Role> role = roleRepository.findBynazivRole(nazivRole);

        if (role.isPresent()) {
            return new ResponseEntity<>(role, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorMsg("Ne postoji rola sa tim nazivom"), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping(value = "/roles/create/{nazivRole}")
    public ResponseEntity<?> createRole(@PathVariable String nazivRole) {
        Optional<Role> role = roleRepository.findBynazivRole(nazivRole);

        if (role.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Postoji vec rola sa tim nazivom!"), HttpStatus.FORBIDDEN);
        }
        else {
            Role newRole = new Role(nazivRole);
            roleRepository.save(newRole);
            return new ResponseEntity<>(newRole, HttpStatus.OK);
        }

    }

    @DeleteMapping(value = "/roles/delete/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            List<User> useri = userRepository.findByRola(role.get());
            if (useri.isEmpty()) {
                roleRepository.deleteById(id);
                return new ResponseEntity<>(role.get(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ErrorMsg("Postoje korisnici sa datom rolom!"), HttpStatus.FORBIDDEN);
            }
        }
        else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena rola sa tim ID-om!"), HttpStatus.FORBIDDEN);
        }
    }
}
