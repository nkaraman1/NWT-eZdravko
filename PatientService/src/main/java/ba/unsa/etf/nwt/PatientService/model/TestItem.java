package ba.unsa.etf.nwt.PatientService.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Stavke")
public class TestItem {
    @Id
    private Long ID;

    @Column
    private String naziv;

    @Column
    private Double ref_min;

    @Column
    private Double ref_max;

    @Column
    private String ref;

    @Column
    private String mjerna_jedinica;

    @ManyToOne
    @JoinColumn(name = "tip_nalaza_id", referencedColumnName = "ID")
    private TestType tip_nalaza_id;

    public TestItem() {
        //this.ID = null;
        this.naziv = null;
        this.ref_min = null;
        this.ref_max = null;
        this.ref = null;
        this.mjerna_jedinica = null;
        this.tip_nalaza_id = null;
    }

    public TestItem(Long ID, String naziv, Double ref_min, Double ref_max, String ref, String mjerna_jedinica, TestType tip_nalaza_id) {
        this.ID = ID;
        this.naziv = naziv;
        this.ref_min = ref_min;
        this.ref_max = ref_max;
        this.ref = ref;
        this.mjerna_jedinica = mjerna_jedinica;
        this.tip_nalaza_id = tip_nalaza_id;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Double getRef_min() {
        return ref_min;
    }

    public void setRef_min(Double ref_min) {
        this.ref_min = ref_min;
    }

    public Double getRef_max() {
        return ref_max;
    }

    public void setRef_max(Double ref_max) {
        this.ref_max = ref_max;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getMjerna_jedinica() {
        return mjerna_jedinica;
    }

    public void setMjerna_jedinica(String mjerna_jedinica) {
        this.mjerna_jedinica = mjerna_jedinica;
    }

    public TestType getTip_nalaza_id() {
        return tip_nalaza_id;
    }

    public void setTip_nalaza_id(TestType tip_nalaza_id) {
        this.tip_nalaza_id = tip_nalaza_id;
    }
}
