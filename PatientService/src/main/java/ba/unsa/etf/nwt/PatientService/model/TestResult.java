package ba.unsa.etf.nwt.PatientService.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name="nalaz_rezultati")
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne
    @JoinColumn(name = "nalaz_id", referencedColumnName = "ID")
    @NotNull(message = "ID nalaza je obavezan.")
    @JsonBackReference
    private Test nalaz;

    @ManyToOne
    @JoinColumn(name = "stavka_id", referencedColumnName = "ID")
    @NotNull(message = "ID stavke je obavezan.")
    @JsonBackReference
    private TestItem stavka;

    @Column
    @NotBlank(message = "Vrijednost je obavezna.")
    private String vrijednost;

    public TestResult(Test nalaz, TestItem stavka, String vrijednost) {
        this.nalaz = nalaz;
        this.stavka = stavka;
        this.vrijednost = vrijednost;
    }

    public TestResult() {
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Test getNalaz() {
        return nalaz;
    }

    public void setNalaz(Test nalaz) {
        this.nalaz = nalaz;
    }

    public TestItem getStavka() {
        return stavka;
    }

    public void setStavka(TestItem stavka) {
        this.stavka = stavka;
    }

    public String getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(String vrijednost) {
        this.vrijednost = vrijednost;
    }
}
