package ba.unsa.etf.nwt.PatientService.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "Pregledi")
public class Examination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column
    @NotBlank(message = "UID pacijenta je obavezan.")
    private String pacijent_uid;

    @Column
    @NotBlank(message = "UID doktora je obavezan.")
    private String doktor_uid;

    @Column
    @Size(min = 10, max = 1000, message
            = "Dijagnoza mora imati između 10 i 1000 karaktera.")
    private String dijagnoza;

    @Column
    @NotNull(message = "Termin pregleda je obavezan.")
    @PastOrPresent(message = "Termin pregleda ne može biti u budućnosti.")
    private LocalDateTime termin_pregleda;

    @OneToMany(mappedBy = "pregled", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Referral> uputnice;

    public Examination() {
        //this.ID = null;
        this.pacijent_uid = null;
        this.doktor_uid = null;
        this.dijagnoza = null;
        this.termin_pregleda = null;
    }

    public Examination(Long ID, String pacijent_uid, String doktor_uid, String dijagnoza, LocalDateTime termin_pregleda) {
        this.ID = ID;
        this.pacijent_uid = pacijent_uid;
        this.doktor_uid = doktor_uid;
        this.dijagnoza = dijagnoza;
        this.termin_pregleda = termin_pregleda;
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



    public List<Referral> getUputnice() {
        return uputnice;
    }


    public void setUputnice(List<Referral> uputnice) {
        this.uputnice = uputnice;
    }
}
