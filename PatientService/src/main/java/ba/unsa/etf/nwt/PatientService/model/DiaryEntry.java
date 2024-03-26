package ba.unsa.etf.nwt.PatientService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "dnevnik_unosi")
public class DiaryEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column
    @NotBlank(message = "UID korisnika je obavezan.")
    private String user_uid;

    @Column
    @PastOrPresent(message = "Datum ne smije biti u budućnosti.")
    private LocalDate datum;

    @Column
    @DecimalMin(value = "0", message = "Visina ne smije biti ispod 0.")
    @DecimalMax(value = "300", message = "Visina ne smije biti iznad 300.")
    private Double visina;

    @Column
    @DecimalMin(value = "0", message = "Težina ne smije biti ispod 0.")
    @DecimalMax(value = "300", message = "Težina ne smije biti iznad 700.")
    private Double tezina;

    @Column
    @DecimalMin(value = "10", message = "BMI ne smije biti ispod 10.")
    @DecimalMax(value = "70", message = "BMI ne smije biti iznad 70.")
    private Double bmi;

    @Column
    @Min(value = 0, message = "Puls ne smije biti ispod 0.")
    @Max(value = 300, message = "Puls ne smije biti iznad 300.")
    private Integer puls;

    @Column
    @DecimalMin(value = "0", message = "Unos vode ne smije biti ispod 0.")
    @DecimalMax(value = "30", message = "Unos vode ne smije biti iznad 30.")
    private Double unos_vode;

    @Column
    @Min(value = 0, message = "Broj koraka ne smije biti ispod 0.")
    @Max(value = 100000, message = "Broj koraka ne smije biti iznad 100 000.")
    private Integer broj_koraka;

    public DiaryEntry() {
        this.ID = null;
        this.user_uid = null;
        this.datum = null;
        this.visina = null;
        this.tezina = null;
        this.bmi = null;
        this.puls = null;
        this.unos_vode = null;
        this.broj_koraka = null;
    }

    public DiaryEntry(Long ID, String user_uid, LocalDate datum, Double visina, Double tezina, Double bmi, Integer puls, Double unos_vode, Integer broj_koraka) {
        this.ID = ID;
        this.user_uid = user_uid;
        this.datum = datum;
        this.visina = visina;
        this.tezina = tezina;
        this.bmi = bmi;
        this.puls = puls;
        this.unos_vode = unos_vode;
        this.broj_koraka = broj_koraka;
    }

    public DiaryEntry(String user_uid, LocalDate datum, Double visina, Double tezina, Double bmi, Integer puls, Double unos_vode, Integer broj_koraka) {
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