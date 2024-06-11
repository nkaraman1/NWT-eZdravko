package ba.unsa.etf.nwt.SurveyService.model;

import ba.unsa.etf.nwt.SurveyService.DTO.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "Korisnici", uniqueConstraints = @UniqueConstraint(columnNames = "UID"))
public class User {
    @Getter
    public enum Spol {
        MUSKO, ZENSKO;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @Column
    @NotBlank(message = "Ime ne smije biti prazno!")
    private String ime;
    @Column
    @NotBlank(message = "Prezime ne smije biti prazno!")
    private String prezime;
    @Column
    @PastOrPresent(message = "Datum rođenja ne smije biti u budućnosti!")
    private LocalDate datum_rodjenja;
    @Column
    @Enumerated(EnumType.ORDINAL)
    private Spol spol;
    @Column
    @Pattern(regexp="[0-9]+", message="Broj telefona smije imati samo brojčane vrijednosti!")
    private String broj_telefona;
    @Column
    @Email(message = "Mail nije ispravan!")
    private String email;
    @Column
    @Size(min = 8, message = "Password mora imati barem 8 znakova!")
    private String password;
    @Column
    private String adresa_stanovanja;
    @Column
    private String slika;
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "ID")
    @NotNull(message = "Korisnik mora imati rolu!")
    private Role rola;
    @Column
    private String UID;
    @Column
    private String broj_knjizice;

    public User(Long ID,
                String ime,
                String prezime,
                LocalDate datum_rodjenja,
                Spol spol,
                String broj_telefona,
                String email,
                String password,
                String adresa_stanovanja,
                String slika,
                Role rola,
                String UID,
                String broj_knjizice) {

        this.ID = ID;
        this.ime = ime;
        this.prezime = prezime;
        this.datum_rodjenja = datum_rodjenja;
        this.spol = spol;
        this.broj_telefona = broj_telefona;
        this.email = email;
        this.password = password;
        this.adresa_stanovanja = adresa_stanovanja;
        this.slika = slika;
        this.rola = rola;
        this.UID = UID;
        this.broj_knjizice = broj_knjizice;
    }

    public User(String ime,
                String prezime,
                LocalDate datum_rodjenja,
                Spol spol,
                String broj_telefona,
                String email,
                String password,
                String adresa_stanovanja,
                String slika,
                Role rola,
                String UID,
                String broj_knjizice) throws Exception {

        this.ime = ime;
        this.prezime = prezime;
        this.datum_rodjenja = datum_rodjenja;
        this.spol = spol;
        this.broj_telefona = broj_telefona;
        this.email = email;
        this.password = password;
        this.adresa_stanovanja = adresa_stanovanja;
        this.slika = slika;
        this.rola = rola;
        this.UID = UID;
        this.broj_knjizice = broj_knjizice;
    }

    public User() {
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
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

    public void setDatum_rodjenja(LocalDate datum_rodjenja) throws Exception {
        this.datum_rodjenja = datum_rodjenja;
    }

    public Spol getSpol() {
        return spol;
    }

    public void setSpol(Spol spol) {
        this.spol = spol;
    }

    public String getBroj_telefona() {
        return broj_telefona;
    }

    public void setBroj_telefona(String broj_telefona) throws Exception {
        this.broj_telefona = broj_telefona;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws Exception {
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

    public Role getRola() {
        return rola;
    }

    public void setRola(Role rola) {
        this.rola = rola;
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

    public void setAllAttributes(UserDTO userDTO) throws Exception {
        this.setIme(userDTO.getIme());
        this.setPrezime(userDTO.getPrezime());
        this.setDatum_rodjenja(userDTO.getDatum_rodjenja());
        this.setSpol(userDTO.getSpol());
        this.setBroj_telefona(userDTO.getBroj_telefona());
        this.setEmail(userDTO.getEmail());
        this.setPassword(userDTO.getPassword());
        this.setAdresa_stanovanja(userDTO.getAdresa_stanovanja());
        this.setSlika(userDTO.getSlika());
        this.setUID(userDTO.getUID());
        this.setBroj_knjizice(userDTO.getBroj_knjizice());
    }
    @Override
    public String toString() {
        return ime + " " + prezime;
    }
}
