package ba.unsa.etf.nwt.SurveyService.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "PonudjeniOdgovori")
public class AnswerOptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "ID")
    private SurveyQuestion anketaPitanje;
    @Column
    private String sadrzaj;

    public AnswerOptions(Long ID, SurveyQuestion anketaPitanje, String sadrzaj) {
        this.ID = ID;
        this.anketaPitanje = anketaPitanje;
        this.sadrzaj = sadrzaj;
    }

    public AnswerOptions() {
        this.ID = null;
        this.anketaPitanje = null;
        this.sadrzaj = null;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public SurveyQuestion getAnketaPitanje() {
        return anketaPitanje;
    }

    public void setAnketaPitanje(SurveyQuestion anketaPitanje) {
        this.anketaPitanje = anketaPitanje;
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