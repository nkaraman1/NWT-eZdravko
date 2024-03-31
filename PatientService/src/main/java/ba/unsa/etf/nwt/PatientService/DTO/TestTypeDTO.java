package ba.unsa.etf.nwt.PatientService.DTO;

import jakarta.validation.constraints.NotBlank;

public class TestTypeDTO {
    @NotBlank(message = "Naziv je obavezan.")
    private String naziv;

    public TestTypeDTO() {
    }

    public TestTypeDTO(String naziv) {
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}
