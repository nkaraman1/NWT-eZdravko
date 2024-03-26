package ba.unsa.etf.nwt.SurveyService.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "AnketePitanja")
public class SurveyQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @ManyToOne
    @JoinColumn(name = "survey_id", referencedColumnName = "ID")
    private Survey anketa;
    @Column
    private String sadrzaj;

    public SurveyQuestion(Long ID, Survey anketa, String sadrzaj) {
        this.ID = ID;
        this.anketa = anketa;
        this.sadrzaj = sadrzaj;
    }

    public SurveyQuestion() {
        this.ID = null;
        this.anketa = null;
        this.sadrzaj = null;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Survey getAnketa() {
        return anketa;
    }

    public void setAnketa(Survey anketa) {
        this.anketa = anketa;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    @Override
    public String toString() {
        return this.getSadrzaj();
    }
}