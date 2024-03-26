package ba.unsa.etf.nwt.PatientService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

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

    public TestType() {
        //this.ID = null;
        this.naziv = null;
    }

    public TestType(Long ID, String naziv) {
        this.ID = ID;
        this.naziv = naziv;
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
}
