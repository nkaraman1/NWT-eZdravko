package ba.unsa.etf.nwt.PatientService.DTO;

import jakarta.validation.constraints.NotBlank;

import java.util.Collections;
import java.util.List;

public class TestTypeDTO {
    private Long ID;
    @NotBlank(message = "Naziv je obavezan.")
    private String naziv;

    private List<Long> nalazi;

    private List<Long> stavke;

    public TestTypeDTO() {
        this.nalazi = Collections.emptyList();
        this.stavke = Collections.emptyList();
    }

    public TestTypeDTO(String naziv) {
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

    public List<Long> getNalazi() {
        return nalazi;
    }

    public void setNalazi(List<Long> nalazi) {
        this.nalazi = nalazi;
    }

    public List<Long> getStavke() {
        return stavke;
    }

    public void setStavke(List<Long> stavke) {
        this.stavke = stavke;
    }
}
