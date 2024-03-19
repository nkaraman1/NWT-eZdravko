package ba.unsa.etf.nwt.PatientService.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Nalazi")
public class Test {
    @Id
    private Long ID;

    // @ManyToOne
    // @JoinColumn(name = "pacijent_uid", referencedColumnName = "UID")
    @Column
    private String pacijent_uid;

    // @ManyToOne
    // @JoinColumn(name = "laborant_uid", referencedColumnName = "UID")
    @Column
    private String laborant_uid;

    // @ManyToOne
    // @JoinColumn(name = "doktor_uid", referencedColumnName = "UID")
    @Column
    private String doktor_uid;

    @ManyToOne
    @JoinColumn(name = "tip_nalaza_id", referencedColumnName = "ID")
    private TestType tip_nalaza_id;

    @Column
    private String dijagnoza;

    @Column
    private String vrijeme_pregleda;

    public Test() {
        //this.ID = null;
        this.pacijent_uid = null;
        this.laborant_uid = null;
        this.doktor_uid = null;
        this.tip_nalaza_id = null;
        this.dijagnoza = null;
        this.vrijeme_pregleda = null;
    }

    public Test(Long ID, String pacijent_uid, String laborant_uid, String doktor_uid, TestType tip_nalaza_id, String dijagnoza, String vrijeme_pregleda) {
        this.ID = ID;
        this.pacijent_uid = pacijent_uid;
        this.laborant_uid = laborant_uid;
        this.doktor_uid = doktor_uid;
        this.tip_nalaza_id = tip_nalaza_id;
        this.dijagnoza = dijagnoza;
        this.vrijeme_pregleda = vrijeme_pregleda;
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

    public String getLaborant_uid() {
        return laborant_uid;
    }

    public void setLaborant_uid(String laborant_uid) {
        this.laborant_uid = laborant_uid;
    }

    public String getDoktor_uid() {
        return doktor_uid;
    }

    public void setDoktor_uid(String doktor_uid) {
        this.doktor_uid = doktor_uid;
    }

    public TestType getTip_nalaza_id() {
        return tip_nalaza_id;
    }

    public void setTip_nalaza_id(TestType tip_nalaza_id) {
        this.tip_nalaza_id = tip_nalaza_id;
    }

    public String getDijagnoza() {
        return dijagnoza;
    }

    public void setDijagnoza(String dijagnoza) {
        this.dijagnoza = dijagnoza;
    }

    public String getVrijeme_pregleda() {
        return vrijeme_pregleda;
    }

    public void setVrijeme_pregleda(String vrijeme_pregleda) {
        this.vrijeme_pregleda = vrijeme_pregleda;
    }
}
