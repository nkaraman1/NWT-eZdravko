package ba.unsa.etf.nwt.PatientService.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class TestDTO {

    private Long ID;
    @NotBlank(message = "UID pacijenta je obavezan.")
    private String pacijent_uid;

    @NotBlank(message = "UID laboranta je obavezan.")
    private String laborant_uid;

    //nije obavezan jer nema doktr_uid vrijednost sve dok doktor ne uzme nalaz da doda dijagnozu
    private String doktor_uid;

    @NotNull(message = "ID tipa nalaza je obavezan.")
    private Long tip_nalaza_id;

    private String dijagnoza;

    private List<Long> rezultati;

    @PastOrPresent(message = "Vrijeme pregleda ne može biti u budućnosti.")
    private LocalDateTime vrijeme_pregleda;

    @PastOrPresent(message = "Vrijeme dijagnoze ne može biti u budućnosti.")
    private LocalDateTime vrijeme_dijagnoze;

    public TestDTO() {
    }

    public TestDTO(String pacijent_uid, String laborant_uid, String doktor_uid, Long tip_nalaza_id, String dijagnoza, LocalDateTime vrijeme_pregleda, LocalDateTime vrijeme_dijagnoze) {
        this.pacijent_uid = pacijent_uid;
        this.laborant_uid = laborant_uid;
        this.doktor_uid = doktor_uid;
        this.tip_nalaza_id = tip_nalaza_id;
        this.dijagnoza = dijagnoza;
        this.vrijeme_pregleda = vrijeme_pregleda;
        this.vrijeme_dijagnoze = vrijeme_dijagnoze;
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

    public String getLaborant_uid() {
        return laborant_uid;
    }

    public void setLaborant_uid(String laborant_uid) {
        this.laborant_uid = laborant_uid;
    }

    public String getDoktor_uid() {
        return doktor_uid;
    }

    public void setDoktor_uid(String doktor_uid) {
        this.doktor_uid = doktor_uid;
    }

    public Long getTip_nalaza_id() {
        return tip_nalaza_id;
    }

    public void setTip_nalaza_id(Long tip_nalaza_id) {
        this.tip_nalaza_id = tip_nalaza_id;
    }

    public String getDijagnoza() {
        return dijagnoza;
    }

    public void setDijagnoza(String dijagnoza) {
        this.dijagnoza = dijagnoza;
    }

    public LocalDateTime getVrijeme_pregleda() {
        return vrijeme_pregleda;
    }

    public void setVrijeme_pregleda(LocalDateTime vrijeme_pregleda) {
        this.vrijeme_pregleda = vrijeme_pregleda;
    }

    public LocalDateTime getVrijeme_dijagnoze() {
        return vrijeme_dijagnoze;
    }

    public void setVrijeme_dijagnoze(LocalDateTime vrijeme_dijagnoze) {
        this.vrijeme_dijagnoze = vrijeme_dijagnoze;
    }

    public List<Long> getRezultati() {
        if(rezultati!=null){
            return rezultati;
        }
        return Collections.emptyList();
    }

    public void setRezultati(List<Long> rezultati) {
        this.rezultati = rezultati;
    }
}
