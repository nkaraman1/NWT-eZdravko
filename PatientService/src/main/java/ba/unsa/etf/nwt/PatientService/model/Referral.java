package ba.unsa.etf.nwt.PatientService.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Uputnice")
public class Referral {
    @Id
    private Long ID;

    @ManyToOne
    @JoinColumn(name = "pregled_id", referencedColumnName = "ID")
    private Examination pregled;

    // @ManyToOne
    // @JoinColumn(name = "specijalista_uid", referencedColumnName = "UID")
    @Column
    private String specijalista_uid;

    @Column
    private String komentar;

    @Column
    private String trajanje;


    public Referral() {
        //this.ID = null;
        this.pregled = null;
        this.specijalista_uid = null;
        this.komentar = null;
        this.trajanje = null;
    }

    public Referral(Long ID, Examination pregled, String specijalista_uid, String komentar, String trajanje) {
        this.ID = ID;
        this.pregled = pregled;
        this.specijalista_uid = specijalista_uid;
        this.komentar = komentar;
        this.trajanje = trajanje;
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

    public String getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(String trajanje) {
        this.trajanje = trajanje;
    }
}
