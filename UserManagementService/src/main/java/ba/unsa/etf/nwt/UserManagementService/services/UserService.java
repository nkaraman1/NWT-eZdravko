package ba.unsa.etf.nwt.UserManagementService.services;

import ba.unsa.etf.nwt.UserManagementService.DTO.UserDTO;
import ba.unsa.etf.nwt.UserManagementService.controllers.UserController;
import ba.unsa.etf.nwt.UserManagementService.exceptions.ErrorMsg;
import ba.unsa.etf.nwt.UserManagementService.exceptions.RoleErrorHandler;
import ba.unsa.etf.nwt.UserManagementService.exceptions.UserErrorHandler;
import ba.unsa.etf.nwt.UserManagementService.model.Role;
import ba.unsa.etf.nwt.UserManagementService.model.User;
import ba.unsa.etf.nwt.UserManagementService.repositories.RoleRepository;
import ba.unsa.etf.nwt.UserManagementService.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserErrorHandler userErrorHandler;
    private final RoleErrorHandler roleErrorHandler;
    @Autowired
    private RoleRepository roleRepository;
    private final Validator validator;

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

    public ResponseEntity<?> getUserByRola(String nazivRole) {
        Optional<Role> rola = roleRepository.findBynazivRole(nazivRole);
        if (rola.isPresent()) {
            return new ResponseEntity<>(userRepository.findByRola(rola.get()), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorMsg(roleErrorHandler.getError(RoleErrorHandler.RoleErrorCode.ROLENAME_NOT_FOUND)), HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<?> createUser(UserDTO userDTO) {
        Errors errors = new BeanPropertyBindingResult(userDTO, "userDTO");
        validator.validate(userDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        var dtoError = userDTO.validateUserDTO();
        if (dtoError.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg(userErrorHandler.getError(dtoError.get())), HttpStatus.FORBIDDEN);
        }

        try {
            Optional<Role> rola = roleRepository.findById(userDTO.getRola_id());
            if (rola.isPresent()) {
                Role selected = rola.get();

                if (!selected.isPotrebanKod() || selected.getKod().equals(userDTO.getRola_kod())) {
                    User user = convertToEntity(userDTO);
                    user.setRola(selected);
                    userRepository.save(user);
                    return new ResponseEntity<>(user, HttpStatus.OK);
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

    public ResponseEntity<?> changeUserData(Long ID, UserDTO userDTO) {
        Errors errors = new BeanPropertyBindingResult(userDTO, "userDTO");
        validator.validate(userDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        var dtoError = userDTO.validateUserDTO();
        if (dtoError.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg(userErrorHandler.getError(dtoError.get())), HttpStatus.FORBIDDEN);
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
                    return new ResponseEntity<>(rezultat, HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(new ErrorMsg(roleErrorHandler.getError(RoleErrorHandler.RoleErrorCode.WRONG_CODE)), HttpStatus.FORBIDDEN);
                }
            }
            else {
                return new ResponseEntity<>(new ErrorMsg(roleErrorHandler.getError(RoleErrorHandler.RoleErrorCode.ROLENAME_NOT_FOUND)), HttpStatus.FORBIDDEN);
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
