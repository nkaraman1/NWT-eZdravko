package ba.unsa.etf.nwt.PatientService.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ExaminationDTO {
    @NotBlank(message = "UID pacijenta je obavezan.")
    private String pacijent_uid;

    @NotBlank(message = "UID doktora je obavezan.")
    private String doktor_uid;

    @Size(min = 10, max = 1000, message
            = "Dijagnoza mora imati između 10 i 1000 karaktera.")
    private String dijagnoza;

    @NotNull(message = "Termin pregleda je obavezan.")
    @PastOrPresent(message = "Termin pregleda ne može biti u budućnosti.")
    private LocalDateTime termin_pregleda;

    public ExaminationDTO() {
    }

    public ExaminationDTO(String pacijent_uid, String doktor_uid, String dijagnoza, LocalDateTime termin_pregleda) {
        this.pacijent_uid = pacijent_uid;
        this.doktor_uid = doktor_uid;
        this.dijagnoza = dijagnoza;
        this.termin_pregleda = termin_pregleda;
    }

    public String getPacijent_uid() {
        return pacijent_uid;
    }

    public void setPacijent_uid(String pacijent_uid) {
        this.pacijent_uid = pacijent_uid;
    }

    public String getDoktor_uid() {
        return doktor_uid;
    }

    public void setDoktor_uid(String doktor_uid) {
        this.doktor_uid = doktor_uid;
    }

    public String getDijagnoza() {
        return dijagnoza;
    }

    public void setDijagnoza(String dijagnoza) {
        this.dijagnoza = dijagnoza;
    }

    public LocalDateTime getTermin_pregleda() {
        return termin_pregleda;
    }

    public void setTermin_pregleda(LocalDateTime termin_pregleda) {
        this.termin_pregleda = termin_pregleda;
    }
}
