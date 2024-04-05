package ba.unsa.etf.nwt.PatientService.DTO;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class DiaryEntryDTO {

    private Long ID;
    @NotBlank(message = "UID korisnika je obavezan.")
    private String user_uid;

    @PastOrPresent(message = "Datum ne smije biti u budućnosti.")
    private LocalDate datum;

    @DecimalMin(value = "0", message = "Visina ne smije biti ispod 0.")
    @DecimalMax(value = "300", message = "Visina ne smije biti iznad 300.")
    private Double visina;

    @DecimalMin(value = "0", message = "Težina ne smije biti ispod 0.")
    @DecimalMax(value = "300", message = "Težina ne smije biti iznad 700.")
    private Double tezina;

    @DecimalMin(value = "10", message = "BMI ne smije biti ispod 10.")
    @DecimalMax(value = "70", message = "BMI ne smije biti iznad 70.")
    private Double bmi;

    @Min(value = 0, message = "Puls ne smije biti ispod 0.")
    @Max(value = 300, message = "Puls ne smije biti iznad 300.")
    private Integer puls;

    @DecimalMin(value = "0", message = "Unos vode ne smije biti ispod 0.")
    @DecimalMax(value = "30", message = "Unos vode ne smije biti iznad 30.")
    private Double unos_vode;

    @Min(value = 0, message = "Broj koraka ne smije biti ispod 0.")
    @Max(value = 100000, message = "Broj koraka ne smije biti iznad 100 000.")
    private Integer broj_koraka;

    public DiaryEntryDTO() {
    }

    public DiaryEntryDTO(String user_uid, LocalDate datum, Double visina, Double tezina, Double bmi, Integer puls, Double unos_vode, Integer broj_koraka) {
        this.user_uid = user_uid;
        this.datum = datum;
        this.visina = visina;
        this.tezina = tezina;
        this.bmi = bmi;
        this.puls = puls;
        this.unos_vode = unos_vode;
        this.broj_koraka = broj_koraka;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public Double getVisina() {
        return visina;
    }

    public void setVisina(Double visina) {
        this.visina = visina;
    }

    public Double getTezina() {
        return tezina;
    }

    public void setTezina(Double tezina) {
        this.tezina = tezina;
    }

    public Double getBmi() {
        return bmi;
    }

    public void setBmi(Double bmi) {
        this.bmi = bmi;
    }

    public Integer getPuls() {
        return puls;
    }

    public void setPuls(Integer puls) {
        this.puls = puls;
    }

    public Double getUnos_vode() {
        return unos_vode;
    }

    public void setUnos_vode(Double unos_vode) {
        this.unos_vode = unos_vode;
    }

    public Integer getBroj_koraka() {
        return broj_koraka;
    }

    public void setBroj_koraka(Integer broj_koraka) {
        this.broj_koraka = broj_koraka;
    }
}
