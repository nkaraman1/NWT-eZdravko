package ba.unsa.etf.nwt.UserManagementService.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Entity
@Data
@Table(name = "Korisnici")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @Column
    private String ime;
    @Column
    private String prezime;
    @Column
    private LocalDate datum_rodjenja;
    @Column
    private Integer spol;
    @Column
    private String broj_telefona;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String adresa_stanovanja;
    @Column
    private String slika;
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "ID")
    private Role rola;
    @Column
    private String UID;
    @Column
    private String broj_knjizice;

    public User(Long ID,
                String ime,
                String prezime,
                LocalDate datum_rodjenja,
                Integer spol,
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
                Integer spol,
                String broj_telefona,
                String email,
                String password,
                String adresa_stanovanja,
                String slika,
                Role rola,
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
        this.rola = rola;
        this.UID = UID;
        this.broj_knjizice = broj_knjizice;
    }

    public User() {
        this.ID = null;
        this.ime = null;
        this.prezime = null;
        this.datum_rodjenja = null;
        this.spol = null;
        this.broj_telefona = null;
        this.email = null;
        this.password = null;
        this.adresa_stanovanja = null;
        this.slika = null;
        this.rola = null;
        this.UID = null;
        this.broj_knjizice = null;
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

    public void setDatum_rodjenja(LocalDate datum_rodjenja) {
        this.datum_rodjenja = datum_rodjenja;
    }

    public Integer getSpol() {
        return spol;
    }

    public void setSpol(Integer spol) {
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

    @Override
    public String toString() {
        return ime + " " + prezime;
    }
}
