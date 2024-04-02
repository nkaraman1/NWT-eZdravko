package ba.unsa.etf.nwt.SurveyService.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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

    @OneToMany(mappedBy = "anketaPitanje", cascade = CascadeType.ALL)
    private List<AnswerOptions> answerOptions;

    public SurveyQuestion(Long ID, Survey anketa, String sadrzaj) {
        this.ID = ID;
        this.anketa = anketa;
        this.sadrzaj = sadrzaj;
    }

    public SurveyQuestion(String sadrzaj) {
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

    @JsonBackReference
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

    @JsonManagedReference
    public List<AnswerOptions> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(List<AnswerOptions> answerOptions) {
        this.answerOptions = answerOptions;
    }

    @Override
    public String toString() {
        return this.getSadrzaj();
    }
}