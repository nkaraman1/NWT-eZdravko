package ba.unsa.etf.nwt.UserManagementService.services;

import ba.unsa.etf.nwt.UserManagementService.DTO.NotificationDTO;
import ba.unsa.etf.nwt.UserManagementService.DTO.UserDTO;
import ba.unsa.etf.nwt.UserManagementService.controllers.UserController;
import ba.unsa.etf.nwt.UserManagementService.exceptions.ErrorMsg;
import ba.unsa.etf.nwt.UserManagementService.exceptions.RoleErrorHandler;
import ba.unsa.etf.nwt.UserManagementService.exceptions.UserErrorHandler;
import ba.unsa.etf.nwt.UserManagementService.feign.NotificationInterface;
import ba.unsa.etf.nwt.UserManagementService.model.Role;
import ba.unsa.etf.nwt.UserManagementService.model.UIMessage;
import ba.unsa.etf.nwt.UserManagementService.model.User;
import ba.unsa.etf.nwt.UserManagementService.model.UserLogin;
import ba.unsa.etf.nwt.UserManagementService.repositories.RoleRepository;
import ba.unsa.etf.nwt.UserManagementService.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserErrorHandler userErrorHandler;
    private final RoleErrorHandler roleErrorHandler;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    private final Validator validator;
    @Autowired
    private NotificationInterface notificationInterface;

    public UserService(UserRepository userRepository, Validator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
        this.userErrorHandler = new UserErrorHandler();
        this.roleErrorHandler = new RoleErrorHandler();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<?> getUserByID(Long ID) {
        Optional<User> user = userRepository.findById(ID);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorMsg(userErrorHandler.getError(UserErrorHandler.UserErrorCode.USER_NOT_FOUND)), HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<?> getUserByImePrezime(String ime, String prezime) {
        List<User> user = userRepository.findByImeAndPrezime(ime, prezime);
        if (user.isEmpty()) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    public ResponseEntity<?> getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Nema usera sa tim mailom!", HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<?> getUserByRola(String nazivRole) {
        Optional<Role> rola = roleRepository.findBynazivRole(nazivRole);
        if (rola.isPresent()) {
            return new ResponseEntity<>(userRepository.findByRola(rola.get()), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorMsg(roleErrorHandler.getError(RoleErrorHandler.RoleErrorCode.ROLENAME_NOT_FOUND)), HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<?> getUserByUID(String UID) {
        Optional<User> user = userRepository.findByUID(UID);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorMsg(userErrorHandler.getError(UserErrorHandler.UserErrorCode.USER_NOT_FOUND)), HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<?> createUser(UserDTO userDTO) {
        Errors errors = new BeanPropertyBindingResult(userDTO, "userDTO");
        validator.validate(userDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        try {
            Optional<Role> rola = roleRepository.findById(userDTO.getRola_id());
            if (rola.isPresent()) {
                Role selected = rola.get();

                if (!selected.isPotrebanKod() || selected.getKod().equals(userDTO.getRola_kod())) {
                    Optional<User> possibleMatches = userRepository.findByEmail(userDTO.getEmail());
                    if (possibleMatches.isEmpty()) {
                        User user = convertToEntity(userDTO);
                        user.setRola(selected);
                        user.setPassword(passwordEncoder.encode(user.getPassword()));
                        userRepository.save(user);
                        return new ResponseEntity<>(user, HttpStatus.OK);
                    }
                    else {
                        return new ResponseEntity<>(new ErrorMsg(userErrorHandler.getError(UserErrorHandler.UserErrorCode.USER_ALREADY_EXISTS)), HttpStatus.FORBIDDEN);
                    }
                }
                else {
                    return new ResponseEntity<>(new ErrorMsg(roleErrorHandler.getError(RoleErrorHandler.RoleErrorCode.WRONG_CODE)), HttpStatus.FORBIDDEN);
                }
            }
            else {
                return new ResponseEntity<>(new ErrorMsg(roleErrorHandler.getError(RoleErrorHandler.RoleErrorCode.ROLENAME_NOT_FOUND)), HttpStatus.FORBIDDEN);
            }

        }
        catch (Exception e) {
            return new ResponseEntity<>(new ErrorMsg(e.getMessage()), HttpStatus.FORBIDDEN);
        }
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    public ResponseEntity<?> changeUserData(Long ID, UserDTO userDTO) {
        Errors errors = new BeanPropertyBindingResult(userDTO, "userDTO");
        validator.validate(userDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        try {
            Optional<Role> rola = roleRepository.findById(userDTO.getRola_id());
            if (rola.isPresent()) {
                Role selected = rola.get();

                if (!selected.isPotrebanKod() || selected.getKod().equals(userDTO.getRola_kod())) {
                    User user = userRepository.getReferenceById(ID);
                    user.setAllAttributes(userDTO);
                    user.setRola(selected);
                    var rezultat = userRepository.save(user);

                    NotificationDTO newNotification = new NotificationDTO("alert", "Vaši podaci su promijenjeni!", user.getUID());
                    notificationInterface.createNotification(newNotification);

                    return new ResponseEntity<>(rezultat, HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(new ErrorMsg(roleErrorHandler.getError(RoleErrorHandler.RoleErrorCode.WRONG_CODE)), HttpStatus.FORBIDDEN);
                }
            }
            else {
                return new ResponseEntity<>(new ErrorMsg(roleErrorHandler.getError(RoleErrorHandler.RoleErrorCode.ID_NOT_FOUND)), HttpStatus.FORBIDDEN);
            }
        }
        catch (EntityNotFoundException enfexc) {
            return new ResponseEntity<>(new ErrorMsg(userErrorHandler.getError(UserErrorHandler.UserErrorCode.USER_NOT_FOUND)), HttpStatus.FORBIDDEN);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ErrorMsg(e.getMessage()), HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<?> changeUserPassword(Long ID, String password) {
        try {
            User user = userRepository.getReferenceById(ID);
            if (password != null && password.length() >= 8) {
                user.setPassword(password);
                var rezultat = userRepository.save(user);

                NotificationDTO newNotification = new NotificationDTO("alert", "Vaša lozinka je promijenjena!", user.getUID());
                notificationInterface.createNotification(newNotification);

                return new ResponseEntity<>(rezultat, HttpStatus.OK);
            }
            else {
                //return new ResponseEntity<>(new ErrorMsg(userErrorHandler.getError(UserErrorHandler.UserErrorCode.PASSWORD_TOO_SHORT)), HttpStatus.FORBIDDEN);
                return new ResponseEntity<>(password + " ne valja", HttpStatus.FORBIDDEN);
            }
        }
        catch (EntityNotFoundException enfexc) {
            return new ResponseEntity<>(new ErrorMsg(userErrorHandler.getError(UserErrorHandler.UserErrorCode.USER_NOT_FOUND)), HttpStatus.FORBIDDEN);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ErrorMsg(e.getMessage()), HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<?> changeUserRole(Long ID, Long roleID) {
        try {
            User user = userRepository.getReferenceById(ID);
            if (roleID != null) {
                Optional<Role> rola = roleRepository.findById(roleID);

                if (rola.isPresent()) {
                    Role selected = rola.get();
                    user.setRola(selected);
                    var rezultat = userRepository.save(user);

                    NotificationDTO newNotification = new NotificationDTO("alert", "Vaša rola je promijenjena u " + selected.getNazivRole() + "!", user.getUID());
                    notificationInterface.createNotification(newNotification);

                    return new ResponseEntity<>(rezultat, HttpStatus.OK);
                }

                else {
                    return new ResponseEntity<>(new ErrorMsg(roleErrorHandler.getError(RoleErrorHandler.RoleErrorCode.ID_NOT_FOUND)), HttpStatus.FORBIDDEN);
                }

            }
            else {
                return new ResponseEntity<>(new ErrorMsg(userErrorHandler.getError(UserErrorHandler.UserErrorCode.ROLE_NULL)), HttpStatus.FORBIDDEN);
            }
        }
        catch (EntityNotFoundException enfexc) {
            return new ResponseEntity<>(new ErrorMsg(userErrorHandler.getError(UserErrorHandler.UserErrorCode.USER_NOT_FOUND)), HttpStatus.FORBIDDEN);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ErrorMsg(e.getMessage()), HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<?> deleteUser(Long ID) {
        Optional<User> user = userRepository.findById(ID);
        if (user.isPresent()) {
            userRepository.deleteById(ID);
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorMsg(userErrorHandler.getError(UserErrorHandler.UserErrorCode.USER_NOT_FOUND)), HttpStatus.FORBIDDEN);
        }
    }

    public List<User> searchUser(String search) {
        ArrayList<User> found = new ArrayList<>();

        var words = search.split("\\s+");
        if (words.length >= 2) {
            List<User> searchImePrezime = userRepository.findByImeAndPrezime(words[0], words[1]);
            List<User> searchPrezimeIme = userRepository.findByImeAndPrezime(words[1], words[0]);
            found.addAll(searchImePrezime);
            found.addAll(searchPrezimeIme);
        }
        else if (words.length == 1) {
            List<User> searchIme = userRepository.findByIme(words[0]);
            List<User> searchPrezime = userRepository.findByPrezime(words[0]);
            found.addAll(searchIme);
            found.addAll(searchPrezime);
        }
        else {
            return userRepository.findAll();
        }

        return found;
    }

    public ResponseEntity<?> userLogin(UserLogin userLogin) {
        List<User> found = userRepository.findByEmailAndPassword(userLogin.getEmail(), userLogin.getPassword());
        if (found.isEmpty()) {
            return new ResponseEntity<>(new ErrorMsg(userErrorHandler.getError(UserErrorHandler.UserErrorCode.WRONG_LOGIN_CREDENTIALS)), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new UIMessage("Prijava uspješna!"), HttpStatus.OK);
    }

    private User convertToEntity(UserDTO userDTO) throws Exception {
        User user = new User();
        user.setIme(userDTO.getIme());
        user.setPrezime(userDTO.getPrezime());
        user.setDatum_rodjenja(userDTO.getDatum_rodjenja());
        user.setSpol(userDTO.getSpol());
        user.setBroj_telefona(userDTO.getBroj_telefona());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setAdresa_stanovanja(userDTO.getAdresa_stanovanja());
        user.setSlika(userDTO.getSlika());
        user.setUID(userDTO.getUID());
        user.setBroj_knjizice(userDTO.getBroj_knjizice());
        return user;
    }
}
