package ba.unsa.etf.nwt.PatientService.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Entity
@Data
@Table(name = "Stavke")
public class TestItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column
    @NotBlank(message = "Naziv stavke je obavezan.")
    private String naziv;

    @Column
    @PositiveOrZero(message = "Referentni minimum ne smije biti negativan.")
    private Double ref_min;

    @Column
    @PositiveOrZero(message = "Referentni maksimum ne smije biti negativan.")
    private Double ref_max;

    @Column
    private String ref;

    @Column
    private String mjerna_jedinica;

    @ManyToOne
    @JoinColumn(name = "tip_nalaza_id", referencedColumnName = "ID")
    @NotNull(message = "ID tipa nalaza je obavezan.")
    @JsonBackReference
    private TestType tip_nalaza;

    @OneToMany(mappedBy = "stavka", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<TestResult> rezultati;


    public TestItem() {
        //this.ID = null;
        this.naziv = null;
        this.ref_min = null;
        this.ref_max = null;
        this.ref = null;
        this.mjerna_jedinica = null;
        this.tip_nalaza = null;
    }

    public TestItem(Long ID, String naziv, Double ref_min, Double ref_max, String ref, String mjerna_jedinica, TestType tip_nalaza) {
        this.ID = ID;
        this.naziv = naziv;
        this.ref_min = ref_min;
        this.ref_max = ref_max;
        this.ref = ref;
        this.mjerna_jedinica = mjerna_jedinica;
        this.tip_nalaza = tip_nalaza;
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

    public TestType getTip_nalaza() {
        return tip_nalaza;
    }

    public void setTip_nalaza(TestType tip_nalaza) {
        this.tip_nalaza = tip_nalaza;
    }

    public List<TestResult> getRezultati() {
        if(rezultati!=null) {
            return rezultati;
        }
        return Collections.emptyList();
    }

    public void setRezultati(List<TestResult> rezultati) {
        this.rezultati = rezultati;
    }
}
