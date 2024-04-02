package ba.unsa.etf.nwt.UserManagementService.DTO;

import ba.unsa.etf.nwt.UserManagementService.exceptions.UserErrorHandler;
import ba.unsa.etf.nwt.UserManagementService.model.Role;
import ba.unsa.etf.nwt.UserManagementService.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Optional;

public class UserDTO {
    @NotBlank(message = "Ime ne smije biti prazno!")
    private String ime;
    @NotBlank(message = "Prezime ne smije biti prazno!")
    private String prezime;
    @PastOrPresent(message = "Datum rođenja ne smije biti u budućnosti!")
    private LocalDate datum_rodjenja;
    @Enumerated(EnumType.ORDINAL)
    private User.Spol spol;
    @Pattern(regexp="[0-9]+", message="Broj telefona smije imati samo brojčane vrijednosti!")
    private String broj_telefona;
    @Email(message = "Mail nije ispravan!")
    private String email;
    @Size(min = 8, message = "Password mora imati barem 8 znakova!")
    private String password;
    private String adresa_stanovanja;
    private String slika;
    @NotNull(message = "Korisnik mora imati rolu!")
    private Long rola_id;
    private String rola_kod;
    private String UID;
    private String broj_knjizice;

    public UserDTO() {
    }

    public UserDTO(String ime,
                   String prezime,
                   LocalDate datum_rodjenja,
                   User.Spol spol,
                   String broj_telefona,
                   String email,
                   String password,
                   String adresa_stanovanja,
                   String slika,
                   Long rola_id,
                   String rola_kod,
                   String UID,
                   String broj_knjizice) {
        this.ime = ime;
        this.prezime = prezime;
        this.datum_rodjenja = datum_rodjenja;
        this.spol = spol;
        this.broj_telefona = broj_telefona;
        this.email = email;
        this.password = password;
        this.adresa_stanovanja = adresa_stanovanja;
        this.slika = slika;
        this.rola_id = rola_id;
        this.rola_kod = rola_kod;
        this.UID = UID;
        this.broj_knjizice = broj_knjizice;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public LocalDate getDatum_rodjenja() {
        return datum_rodjenja;
    }

    public void setDatum_rodjenja(LocalDate datum_rodjenja) {
        this.datum_rodjenja = datum_rodjenja;
    }

    public User.Spol getSpol() {
        return spol;
    }

    public void setSpol(User.Spol spol) {
        this.spol = spol;
    }

    public String getBroj_telefona() {
        return broj_telefona;
    }

    public void setBroj_telefona(String broj_telefona) {
        this.broj_telefona = broj_telefona;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdresa_stanovanja() {
        return adresa_stanovanja;
    }

    public void setAdresa_stanovanja(String adresa_stanovanja) {
        this.adresa_stanovanja = adresa_stanovanja;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public Long getRola_id() {
        return rola_id;
    }

    public void setRola_id(Long rola_id) {
        this.rola_id = rola_id;
    }

    public String getRola_kod() {
        return rola_kod;
    }

    public void setRola_kod(String rola_kod) {
        this.rola_kod = rola_kod;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getBroj_knjizice() {
        return broj_knjizice;
    }

    public void setBroj_knjizice(String broj_knjizice) {
        this.broj_knjizice = broj_knjizice;
    }

    public Optional<UserErrorHandler.UserErrorCode> validateUserDTO() {
        if (this.ime.isBlank()) {
            return Optional.of(UserErrorHandler.UserErrorCode.NAME_BLANK);
        }
        if (this.prezime.isBlank()) {
            return Optional.of(UserErrorHandler.UserErrorCode.SURNAME_BLANK);
        }
        if (!ispravanDatum(this.datum_rodjenja)) {
            return Optional.of(UserErrorHandler.UserErrorCode.DATE_IN_FUTURE);
        }
        if (!ispravanBrojTelefona(this.broj_telefona)) {
            return Optional.of(UserErrorHandler.UserErrorCode.PHONE_NOT_NUMERIC);
        }
        if (!ispravanMail(this.email)) {
            return Optional.of(UserErrorHandler.UserErrorCode.EMAIL_INVALID);
        }
        if (!ispravanPassword(this.password)) {
            return Optional.of(UserErrorHandler.UserErrorCode.PASSWORD_TOO_SHORT);
        }
        if (this.rola_id == null) {
            return Optional.of(UserErrorHandler.UserErrorCode.ROLE_NULL);
        }

        return Optional.empty();
    }

    private static boolean ispravanDatum(LocalDate datum) {
        return !datum.isAfter(LocalDate.now());
    }

    private static boolean ispravanBrojTelefona(String broj) {
        return broj.matches("[0-9]+");
    }

    private static boolean ispravanPassword(String password) {
        return password.length() >= 8;
    }

    private static boolean ispravanMail(String email) {
        var regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        var pattern = java.util.regex.Pattern.compile(regexPattern);
        return pattern.matcher(email).matches();
    }
}
