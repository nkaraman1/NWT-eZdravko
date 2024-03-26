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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
public class UserController {
    private final UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private static boolean provjeriDatum(LocalDate datum) {
        return !datum.isAfter(LocalDate.now());
    }

    private static boolean provjeriBrojTelefona(String broj) {
        return broj.matches("[0-9]+");
    }

    private static boolean provjeriPassword(String password) {
        return password.length() >= 8;
    }

    private static boolean provjeriMail(String email) {
        var regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        var pattern = Pattern.compile(regexPattern);
        return pattern.matcher(email).matches();
    }

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
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
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

    @PostMapping(value="/users/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            if (user.getIme().isBlank()) {
                throw new Exception("Ime je prazno!");
            }
            if (user.getPrezime().isBlank()) {
                throw new Exception("Prezime je prazno!");
            }
            if (user.getAdresa_stanovanja().isBlank()) {
                throw new Exception("Adresa stanovanja je prazna!");
            }
            if (user.getBroj_knjizice().isBlank()) {
                throw new Exception("Broj knjizice je prazan!");
            }
            if (!UserController.provjeriMail(user.getEmail())) {
                throw new Exception("Email nije ispravan!");
            }
            if (!UserController.provjeriDatum(user.getDatum_rodjenja())) {
                throw new Exception("Datum rodjenja nije ispravan!");
            }
            if (!UserController.provjeriPassword(user.getPassword())) {
                throw new Exception("Password mora sadržavati barem 8 znakova!");
            }
            if (!UserController.provjeriBrojTelefona(user.getBroj_telefona())) {
                throw new Exception("Broj telefona nije ispravan!");
            }

            userRepository.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(new ErrorMsg(e.getMessage()), HttpStatus.FORBIDDEN);
        }

    }

    @PutMapping(value="/users/change/{ID}/ime/{ime}")
    public ResponseEntity<?> changeUserIme(@PathVariable Long ID, @PathVariable String ime) {
        try {
            if (ime.isBlank()) {
                throw new Exception("Ime je prazno!");
            }
            User selected = userRepository.getReferenceById(ID);
            selected.setIme(ime);
            var rezultat = userRepository.save(selected);
            return new ResponseEntity<>(rezultat, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ErrorMsg("Neispravni parametri!"), HttpStatus.FORBIDDEN);
        }

    }

    @PutMapping(value="/users/change/{ID}/prezime/{prezime}")
    public ResponseEntity<?> changeUserPrezime(@PathVariable Long ID, @PathVariable String prezime) {
        try {
            if (prezime.isBlank()) {
                throw new Exception("Prezime je prazno!");
            }
            User selected = userRepository.getReferenceById(ID);
            selected.setPrezime(prezime);
            var rezultat = userRepository.save(selected);
            return new ResponseEntity<>(rezultat, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ErrorMsg("Neispravni parametri!"), HttpStatus.FORBIDDEN);
        }

    }

    @PutMapping(value="/users/change/{ID}/broj-telefona/{broj_telefona}")
    public ResponseEntity<?> changeUserTelefon(@PathVariable Long ID, @PathVariable String broj_telefona) {
        try {
            if (!UserController.provjeriBrojTelefona(broj_telefona)) {
                throw new Exception("Broj telefona nije ispravan!");
            }
            User selected = userRepository.getReferenceById(ID);
            selected.setBroj_telefona(broj_telefona);
            var rezultat = userRepository.save(selected);
            return new ResponseEntity<>(rezultat, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ErrorMsg("Neispravni parametri!"), HttpStatus.FORBIDDEN);
        }

    }

    @PutMapping(value="/users/change/{ID}/email/{email}")
    public ResponseEntity<?> changeUserEmail(@PathVariable Long ID, @PathVariable String email) {
        try {
            if (!UserController.provjeriMail(email)) {
                throw new Exception("Email nije ispravan!");
            }
            User selected = userRepository.getReferenceById(ID);
            selected.setEmail(email);
            var rezultat = userRepository.save(selected);
            return new ResponseEntity<>(rezultat, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ErrorMsg("Neispravni parametri!"), HttpStatus.FORBIDDEN);
        }

    }

    @PutMapping(value="/users/change/{ID}/password/{password}")
    public ResponseEntity<?> changeUserPassword(@PathVariable Long ID, @PathVariable String password) {
        try {
            if (!UserController.provjeriPassword(password)) {
                throw new Exception("Password mora sadržavati barem 8 znakova!");
            }
            User selected = userRepository.getReferenceById(ID);
            selected.setPassword(password);
            var rezultat = userRepository.save(selected);
            return new ResponseEntity<>(rezultat, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ErrorMsg("Neispravni parametri!"), HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping(value="/users/change/{ID}/adresa/{adresa_stanovanja}")
    public ResponseEntity<?> changeUserAdresa(@PathVariable Long ID, @PathVariable String adresa_stanovanja) {
        try {
            if (adresa_stanovanja.isBlank()) {
                throw new Exception("Adresa stanovanja je prazna!");
            }
            User selected = userRepository.getReferenceById(ID);
            selected.setAdresa_stanovanja(adresa_stanovanja);
            var rezultat = userRepository.save(selected);
            return new ResponseEntity<>(rezultat, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ErrorMsg("Neispravni parametri!"), HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping(value="/users/change/{ID}/slika/{slika}")
    public ResponseEntity<?> changeUserSlika(@PathVariable Long ID, @PathVariable String slika) {
        try {
            User selected = userRepository.getReferenceById(ID);
            selected.setSlika(slika);
            var rezultat = userRepository.save(selected);
            return new ResponseEntity<>(rezultat, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ErrorMsg("Neispravni parametri!"), HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping(value="/users/change/{ID}/knjizica/{broj_knjizice}")
    public ResponseEntity<?> changeUserKnjizica(@PathVariable Long ID, @PathVariable String broj_knjizice) {
        try {
            User selected = userRepository.getReferenceById(ID);
            if (broj_knjizice.isBlank()) {
                throw new Exception("Broj knjizice je prazan!");
            }
            selected.setBroj_knjizice(broj_knjizice);
            var rezultat = userRepository.save(selected);
            return new ResponseEntity<>(rezultat, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ErrorMsg("Neispravni parametri!"), HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping(value="/users/change/{ID}/rola/{nazivRole}")
    public ResponseEntity<?> changeUserRola(@PathVariable Long ID, String nazivRole) {
        try {
            Optional<Role> rola = roleRepository.findBynazivRole(nazivRole);
            if (rola.isEmpty()) {
                throw new Exception("Rola ne postoji!");
            }
            User selected = userRepository.getReferenceById(ID);
            selected.setRola(rola.get());
            var rezultat = userRepository.save(selected);
            return new ResponseEntity<>(rezultat, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ErrorMsg("Neispravni parametri!"), HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping(value="/users/delete/{ID}")
    public ResponseEntity<?> deleteUser(@PathVariable Long ID) {
        Optional<User> user = userRepository.findById(ID);
        if (user.isPresent()) {
            userRepository.deleteById(ID);
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen korisnik sa datim ID-om!"), HttpStatus.FORBIDDEN);
        }
    }
}
