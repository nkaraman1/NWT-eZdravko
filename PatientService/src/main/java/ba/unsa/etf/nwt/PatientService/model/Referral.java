package ba.unsa.etf.nwt.PatientService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "Uputnice")
public class Referral {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;

    @ManyToOne
    @JoinColumn(name = "pregled_id", referencedColumnName = "ID")
    @NotBlank(message = "ID pregleda je obavezan.")
    private Examination pregled;

    @Column
    @NotBlank(message = "UID specijaliste je obavezan.")
    private String specijalista_uid;

    @Column
    @Size(min = 10, max = 1000, message
            = "Komentar mora imati između 10 i 1000 karaktera.")
    private String komentar;

    @Column
    @Future(message = "Datum isteka mora biti u budućnosti.")
    private LocalDate datum_isteka;


    public Referral() {
        //this.ID = null;
        this.pregled = null;
        this.specijalista_uid = null;
        this.komentar = null;
        this.datum_isteka = null;
    }

    public Referral(Long ID, Examination pregled, String specijalista_uid, String komentar, LocalDate datum_isteka) {
        this.ID = ID;
        this.pregled = pregled;
        this.specijalista_uid = specijalista_uid;
        this.komentar = komentar;
        this.datum_isteka = datum_isteka;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Examination getPregled() {
        return pregled;
    }

    public void setPregled(Examination pregled) {
        this.pregled = pregled;
    }

    public String getSpecijalista_uid() {
        return specijalista_uid;
    }

    public void setSpecijalista_uid(String specijalista_uid) {
        this.specijalista_uid = specijalista_uid;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public LocalDate getDatum_isteka() {
        return datum_isteka;
    }

    public void setDatum_isteka(LocalDate datum_isteka) {
        this.datum_isteka = datum_isteka;
    }
}
