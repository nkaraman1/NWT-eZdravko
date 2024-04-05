package ba.unsa.etf.nwt.PatientService.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@Table(name = "Nalazi")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column
    @NotBlank(message = "UID pacijenta je obavezan.")
    private String pacijent_uid;

    @Column
    @NotBlank(message = "UID laboranta je obavezan.")
    private String laborant_uid;

    @Column
    //nije obavezan jer nema doktr_uid vrijednost sve dok doktor ne uzme nalaz da doda dijagnozu
    private String doktor_uid;

    @ManyToOne
    @JoinColumn(name = "tip_nalaza_id", referencedColumnName = "ID")
    @NotNull(message = "ID tipa nalaza je obavezan.")
    @JsonBackReference
    private TestType tip_nalaza;

    @Column
    //@NotBlank(message = "Dijagnoza je obavezna.")
    private String dijagnoza;

    @Column
    @PastOrPresent(message = "Vrijeme pregleda ne može biti u budućnosti.")
    private LocalDateTime vrijeme_pregleda;

    @Column
    @PastOrPresent(message = "Vrijeme dijagnoze ne može biti u budućnosti.")
    private LocalDateTime vrijeme_dijagnoze;

    @OneToMany(mappedBy = "nalaz", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<TestResult> rezultati;

    public Test() {
        //this.ID = null;
        this.pacijent_uid = null;
        this.laborant_uid = null;
        this.doktor_uid = null;
        this.tip_nalaza = null;
        this.dijagnoza = "/";
        this.vrijeme_pregleda = null;
        this.vrijeme_dijagnoze = null;
    }

    public Test(Long ID, String pacijent_uid, String laborant_uid, String doktor_uid, TestType tip_nalaza_id, String dijagnoza, LocalDateTime vrijeme_pregleda, LocalDateTime vrijeme_dijagnoze) {
        this.ID = ID;
        this.pacijent_uid = pacijent_uid;
        this.laborant_uid = laborant_uid;
        this.doktor_uid = doktor_uid;
        this.tip_nalaza = tip_nalaza_id;
        this.dijagnoza = dijagnoza;
        this.vrijeme_pregleda = vrijeme_pregleda;
        this.vrijeme_dijagnoze = vrijeme_dijagnoze;
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

    public TestType getTip_nalaza() {
        return tip_nalaza;
    }

    public void setTip_nalaza(TestType tip_nalaza) {
        this.tip_nalaza = tip_nalaza;
    }

    public String getDijagnoza() {
        return dijagnoza;
    }

    public void setDijagnoza(String dijagnoza) {
        this.dijagnoza = dijagnoza;
    }

    public LocalDateTime getVrijeme_pregleda() {
        return vrijeme_pregleda;
    }

    public void setVrijeme_pregleda(LocalDateTime vrijeme_pregleda) {
        this.vrijeme_pregleda = vrijeme_pregleda;
    }

    public LocalDateTime getVrijeme_dijagnoze() {
        return vrijeme_dijagnoze;
    }

    public void setVrijeme_dijagnoze(LocalDateTime vrijeme_dijagnoze) {
        this.vrijeme_dijagnoze = vrijeme_dijagnoze;
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
