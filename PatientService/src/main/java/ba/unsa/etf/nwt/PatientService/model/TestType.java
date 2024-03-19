package ba.unsa.etf.nwt.PatientService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "tip_nalaza")
public class TestType {
    @Id
    private Long ID;

    @Column
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
