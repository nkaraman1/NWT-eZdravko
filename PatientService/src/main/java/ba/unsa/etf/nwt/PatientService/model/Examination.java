package ba.unsa.etf.nwt.PatientService.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "Pregledi")
public class Examination {
    @Id
    private Long ID;
    // @ManyToOne
    // @JoinColumn(name = "pacijent_uid", referencedColumnName = "UID")
    @Column
    private String pacijent_uid;
    // @ManyToOne
    // @JoinColumn(name = "doktor_uid", referencedColumnName = "UID")
    @Column
    private String doktor_uid;
    @Column
    private String dijagnoza;
    @Column
    //private LocalDate termin_pregleda;
    private String termin_pregleda;

    public Examination() {
        //this.ID = null;
        this.pacijent_uid = null;
        this.doktor_uid = null;
        this.dijagnoza = null;
        this.termin_pregleda = null;
    }

    public Examination(Long ID, String pacijent_uid, String doktor_uid, String dijagnoza, String termin_pregleda) {
        this.ID = ID;
        this.pacijent_uid = pacijent_uid;
        this.doktor_uid = doktor_uid;
        this.dijagnoza = dijagnoza;
        this.termin_pregleda = termin_pregleda;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getPacijent_uid() {
        return pacijent_uid;
    }

    public void setPacijent_uid(String pacijent_uid) {
        this.pacijent_uid = pacijent_uid;
    }

    public String getDoktor_uid() {
        return doktor_uid;
    }

    public void setDoktor_uid(String doktor_uid) {
        this.doktor_uid = doktor_uid;
    }

    public String getDijagnoza() {
        return dijagnoza;
    }

    public void setDijagnoza(String dijagnoza) {
        this.dijagnoza = dijagnoza;
    }

    public String getTermin_pregleda() {
        return termin_pregleda;
    }

    public void setTermin_pregleda(String termin_pregleda) {
        this.termin_pregleda = termin_pregleda;
    }
}
