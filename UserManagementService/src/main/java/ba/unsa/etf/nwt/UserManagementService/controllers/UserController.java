package ba.unsa.etf.nwt.UserManagementService.controllers;

import ba.unsa.etf.nwt.UserManagementService.DTO.UserDTO;
import ba.unsa.etf.nwt.UserManagementService.exceptions.ErrorMsg;
import ba.unsa.etf.nwt.UserManagementService.model.Role;
import ba.unsa.etf.nwt.UserManagementService.model.User;
import ba.unsa.etf.nwt.UserManagementService.model.UserLogin;
import ba.unsa.etf.nwt.UserManagementService.repositories.RoleRepository;
import ba.unsa.etf.nwt.UserManagementService.repositories.UserRepository;
import ba.unsa.etf.nwt.UserManagementService.services.UserService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value="/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value="/users/id/{ID}")
    public ResponseEntity<?> getUserByID(@PathVariable Long ID) {
        return userService.getUserByID(ID);
    }

    @GetMapping(value="/users/uid/{UID}")
    public ResponseEntity<?> getUserByUID(@PathVariable String UID) {
        return userService.getUserByUID(UID);
    }

    @GetMapping(value="/users/ime/{ime}/prezime/{prezime}")
    public ResponseEntity<?> getUserByImePrezime(@PathVariable String ime, @PathVariable String prezime) {
        return userService.getUserByImePrezime(ime, prezime);
    }

    @GetMapping(value="/users/role/{nazivRole}")
    public ResponseEntity<?> getUserByRola(@PathVariable String nazivRole) {
        return userService.getUserByRola(nazivRole);
    }

    @GetMapping(value="/users/search")
    public List<User> searchUsers(@RequestParam(name = "query", required = false) String search) {
        return userService.searchUser(search);
    }

    @PostMapping(value="/users/login")
    public ResponseEntity<?> login(@RequestBody UserLogin userLogin) {
        return userService.userLogin(userLogin);
    }

    @PostMapping(value="/users/create")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PostMapping("/token")
    public String getToken(@RequestBody UserLogin authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return userService.generateToken(authRequest.getEmail());
        } else {
            throw new RuntimeException("invalid access");
        }
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        userService.validateToken(token);
        return "Token is valid";
    }
    @PutMapping(value="/users/change/{ID}")
    public ResponseEntity<?> changeUserData(@PathVariable Long ID, @RequestBody UserDTO userDTO) {
        return userService.changeUserData(ID, userDTO);
    }

    @DeleteMapping(value="/users/delete/{ID}")
    public ResponseEntity<?> deleteUser(@PathVariable Long ID) {
        return userService.deleteUser(ID);
    }

    @PatchMapping(value="/users/change/{ID}/password/{password}")
    public ResponseEntity<?> changePassword(@PathVariable Long ID, @PathVariable String password) {
        return userService.changeUserPassword(ID, password);
    }

    @PatchMapping(value="/users/change/{ID}/role/{roleID}")
    public ResponseEntity<?> changeRole(@PathVariable Long ID, @PathVariable Long roleID) {
        return userService.changeUserRole(ID, roleID);
    }
}
