package ba.unsa.etf.nwt.PatientService.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Collections;
import java.util.List;

public class TestItemDTO {

    private Long ID;
    @NotBlank(message = "Naziv stavke je obavezan.")
    private String naziv;

    @PositiveOrZero(message = "Referentni minimum ne smije biti negativan.")
    private Double ref_min;

    @PositiveOrZero(message = "Referentni maksimum ne smije biti negativan.")
    private Double ref_max;

    private String ref;

    private String mjerna_jedinica;

    @NotNull()
    private Long tip_nalaza_id;

    private List<Long> rezultati;

    public TestItemDTO() {
    }

    public TestItemDTO(String naziv, Double ref_min, Double ref_max, String ref, String mjerna_jedinica, Long tip_nalaza_id) {
        this.naziv = naziv;
        this.ref_min = ref_min;
        this.ref_max = ref_max;
        this.ref = ref;
        this.mjerna_jedinica = mjerna_jedinica;
        this.tip_nalaza_id = tip_nalaza_id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Double getRef_min() {
        return ref_min;
    }

    public void setRef_min(Double ref_min) {
        this.ref_min = ref_min;
    }

    public Double getRef_max() {
        return ref_max;
    }

    public void setRef_max(Double ref_max) {
        this.ref_max = ref_max;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getMjerna_jedinica() {
        return mjerna_jedinica;
    }

    public void setMjerna_jedinica(String mjerna_jedinica) {
        this.mjerna_jedinica = mjerna_jedinica;
    }

    public Long getTip_nalaza_id() {
        return tip_nalaza_id;
    }

    public void setTip_nalaza_id(Long tip_nalaza_id) {
        this.tip_nalaza_id = tip_nalaza_id;
    }

    public List<Long> getRezultati() {
        if(rezultati!=null)
            return rezultati;
        return Collections.emptyList();
    }

    public void setRezultati(List<Long> rezultati) {
        this.rezultati = rezultati;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
}
