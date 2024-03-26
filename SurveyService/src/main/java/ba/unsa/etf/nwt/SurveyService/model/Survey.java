package ba.unsa.etf.nwt.SurveyService.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Ankete")
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @Column
    private String user_uid;
    @Column
    private String naslov;
    @Column
    private String opis;
    @Column
    private Boolean status;

    public Survey(Long ID, String UID_korisnika, String naslov, String opis, Boolean status) {
        this.ID = ID;
        this.user_uid = UID_korisnika;
        this.naslov = naslov;
        this.opis = opis;
        this.status = status;
    }

    public Survey() {
        this.ID = null;
        this.user_uid = null;
        this.naslov = null;
        this.opis = null;
        this.status = null;
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

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.getNaslov();
    }
}