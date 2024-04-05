package ba.unsa.etf.nwt.PatientService.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TestResultDTO {

    private Long ID;
    @NotBlank(message = "Vrijednost je obavezna.")
    private String vrijednost;

    @NotNull(message = "ID nalaza je obavezan.")
    private Long nalaz_id;

    @NotNull(message = "ID stavke je obavezan.")
    private Long stavka_id;

    public TestResultDTO() {
    }

    public TestResultDTO(String vrijednost, Long nalaz_id, Long stavka_id) {
        this.vrijednost = vrijednost;
        this.nalaz_id = nalaz_id;
        this.stavka_id = stavka_id;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(String vrijednost) {
        this.vrijednost = vrijednost;
    }

    public Long getNalaz_id() {
        return nalaz_id;
    }

    public void setNalaz_id(Long nalaz_id) {
        this.nalaz_id = nalaz_id;
    }

    public Long getStavka_id() {
        return stavka_id;
    }

    public void setStavka_id(Long stavka_id) {
        this.stavka_id = stavka_id;
    }
}
