package ba.unsa.etf.nwt.PatientService.DTO;

import ba.unsa.etf.nwt.PatientService.model.Examination;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ReferralDTO {

    private Long ID;

    @NotNull(message = "ID pregleda je obavezan.")
    private Long pregled_id;

    @NotBlank(message = "UID specijaliste je obavezan.")
    private String specijalista_uid;

    @Size(min = 10, max = 1000, message
            = "Komentar mora imati između 10 i 1000 karaktera.")
    private String komentar;

    @Future(message = "Datum isteka mora biti u budućnosti.")
    private LocalDate datum_isteka;

    public ReferralDTO() {
    }

    public ReferralDTO(Long pregled_id, String specijalista_uid, String komentar, LocalDate datum_isteka) {
        this.pregled_id = pregled_id;
        this.specijalista_uid = specijalista_uid;
        this.komentar = komentar;
        this.datum_isteka = datum_isteka;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getPregled_id() {
        return pregled_id;
    }

    public void setPregled_id(Long pregled_id) {
        this.pregled_id = pregled_id;
    }

    public String getSpecijalista_uid() {
        return specijalista_uid;
    }

    public void setSpecijalista_uid(String specijalista_uid) {
        this.specijalista_uid = specijalista_uid;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public LocalDate getDatum_isteka() {
        return datum_isteka;
    }

    public void setDatum_isteka(LocalDate datum_isteka) {
        this.datum_isteka = datum_isteka;
    }
}
