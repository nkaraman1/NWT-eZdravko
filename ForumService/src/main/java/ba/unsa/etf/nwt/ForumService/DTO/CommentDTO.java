package ba.unsa.etf.nwt.ForumService.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CommentDTO {
    private Long ID;
    @NotNull(message = "ID pitanja je obavezan.")
    private Long questionId;
    @NotBlank(message = "UID korisnika je obavezan.")
    private String userUid;
    @NotBlank(message = "Sadržaj komentara je obavezan.")
    private String sadrzaj;
    @NotNull(message = "Status anonimnosti je obavezan.")
    @DecimalMin(value = "0", message = "Status anonimnosti može biti ili 0 ili 1.")
    @DecimalMax(value = "1", message = "Status anonimnosti može biti ili 0 ili 1.")
    private Integer anonimnost;

    public CommentDTO(Long ID, Long questionId, String userUid, String sadrzaj, Integer anonimnost) {
        this.ID = ID;
        this.questionId = questionId;
        this.userUid = userUid;
        this.sadrzaj = sadrzaj;
        this.anonimnost = anonimnost;
    }

    public CommentDTO() {

    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    public Integer getAnonimnost() {
        return anonimnost;
    }

    public void setAnonimnost(Integer anonimnost) {
        this.anonimnost = anonimnost;
    }
}
