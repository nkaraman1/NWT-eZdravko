package ba.unsa.etf.nwt.PatientService.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Entity
@Data
@Table(name = "tip_nalaza")
public class TestType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column
    @NotBlank(message = "Naziv je obavezan.")
    private String naziv;

    @OneToMany(mappedBy = "tip_nalaza", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Test> nalazi;

    @OneToMany(mappedBy = "tip_nalaza", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<TestItem> stavke;

    public TestType() {
        //this.ID = null;
        this.naziv = null;
        this.nalazi = Collections.emptyList();
        this.stavke = Collections.emptyList();
    }

    public TestType(String naziv) {
        this.naziv = naziv;
        this.nalazi = Collections.emptyList();
        this.stavke = Collections.emptyList();
    }

    public TestType(Long ID, String naziv) {
        this.ID = ID;
        this.naziv = naziv;
        this.nalazi = Collections.emptyList();
        this.stavke = Collections.emptyList();
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

    public List<Test> getNalazi() {
        return nalazi;
    }

    public void setNalazi(List<Test> nalazi) {
        this.nalazi = nalazi;
    }

    public List<TestItem> getStavke() {
        return stavke;
    }

    public void setStavke(List<TestItem> stavke) {
        this.stavke = stavke;
    }
}
