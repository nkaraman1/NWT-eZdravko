package ba.unsa.etf.nwt.SurveyService.DTO;

import jakarta.validation.constraints.NotNull;

public class SurveyAnswerDTO {
    @NotNull(message = "ID odgovora je obavezan.")
    private Long answerId;

    public SurveyAnswerDTO(Long answerId) {
        this.answerId = answerId;
    }

    public SurveyAnswerDTO() {
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }
}
