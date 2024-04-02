package ba.unsa.etf.nwt.SurveyService.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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
    private Integer status;

    @OneToMany(mappedBy = "anketa", cascade = CascadeType.ALL)
    private List<SurveyQuestion> surveyQuestions;

    public Survey(Long ID, String UID_korisnika, String naslov, String opis, Integer status) {
        this.ID = ID;
        this.user_uid = UID_korisnika;
        this.naslov = naslov;
        this.opis = opis;
        this.status = status;
    }

    public Survey(String user_uid, String naslov, String opis, Integer status) {
        this.user_uid = user_uid;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonManagedReference
    public List<SurveyQuestion> getSurveyQuestions() {
        return surveyQuestions;
    }

    public void setSurveyQuestions(List<SurveyQuestion> surveyQuestions) {
        this.surveyQuestions = surveyQuestions;
    }

    @Override
    public String toString() {
        return this.getNaslov();
    }
}