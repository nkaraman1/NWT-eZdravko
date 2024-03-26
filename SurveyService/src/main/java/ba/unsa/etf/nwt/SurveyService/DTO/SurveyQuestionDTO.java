package ba.unsa.etf.nwt.SurveyService.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SurveyQuestionDTO {
    @NotNull(message = "ID ankete je obavezan.")
    private Long surveyId;

    @NotBlank(message = "Sadrzaj ankete je obavezan.")
    private String sadrzaj;

    public SurveyQuestionDTO(Long surveyId, String sadrzaj) {
        this.surveyId = surveyId;
        this.sadrzaj = sadrzaj;
    }

    public SurveyQuestionDTO() {
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }
}
