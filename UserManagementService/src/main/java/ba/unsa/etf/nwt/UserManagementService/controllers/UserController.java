package ba.unsa.etf.nwt.UserManagementService.controllers;

import ba.unsa.etf.nwt.UserManagementService.model.ErrorMsg;
import ba.unsa.etf.nwt.UserManagementService.model.Role;
import ba.unsa.etf.nwt.UserManagementService.model.User;
import ba.unsa.etf.nwt.UserManagementService.repositories.RoleRepository;
import ba.unsa.etf.nwt.UserManagementService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping(value="/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping(value="/users/id/{ID}")
    public ResponseEntity<?> getUserByID(@PathVariable Long ID) {
        Optional<User> user = userRepository.findById(ID);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen korisnik sa tim ID-om!"), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping(value="/users/ime/{ime}/prezime/{prezime}")
    public ResponseEntity<?> getUserByImePrezime(@PathVariable String ime, @PathVariable String prezime) {
        List<User> user = userRepository.findByImeAndPrezime(ime, prezime);
        if (user.isEmpty()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen nijedan korisnik sa tim parametrima!"), HttpStatus.FORBIDDEN);
        }
        else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @GetMapping(value="/users/role/{nazivRole}")
    public ResponseEntity<?> getUserByRola(@PathVariable String nazivRole) {
        Optional<Role> rola = roleRepository.findBynazivRole(nazivRole);
        if (rola.isPresent()) {
            return new ResponseEntity<>(userRepository.findByRola(rola.get()), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena proslijedjena rola!"), HttpStatus.FORBIDDEN);
        }
    }
}
