package ba.unsa.etf.nwt.SurveyService.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AnswerOptionsDTO {
    @NotNull(message = "ID pitanja je obavezan.")
    private Long questionId;

    @NotBlank(message = "Sadrzaj je obavezan.")
    private String sadrzaj;

    public AnswerOptionsDTO(Long questionId, String sadrzaj) {
        this.questionId = questionId;
        this.sadrzaj = sadrzaj;
    }

    public AnswerOptionsDTO() {
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }
}