package ba.unsa.etf.nwt.SurveyService.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "AnketaOdgovori")
public class SurveyAnswer {
    @Id
    private Long ID;
    @OneToOne
    @JoinColumn(name = "answer_id", referencedColumnName = "ID")
    private AnswerOptions anketaOdgovor;

    public SurveyAnswer(Long ID, AnswerOptions anketaOdgovor) {
        this.ID = ID;
        this.anketaOdgovor = anketaOdgovor;
    }

    public SurveyAnswer() {
        this.ID = null;
        this.anketaOdgovor = null;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public AnswerOptions getAnketaOdgovor() {
        return anketaOdgovor;
    }

    public void setAnketaOdgovor(AnswerOptions anketaOdgovor) {
        this.anketaOdgovor = anketaOdgovor;
    }

    @Override
    public String toString() {
        return anketaOdgovor.toString();
    }
}