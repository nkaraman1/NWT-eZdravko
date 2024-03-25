package ba.unsa.etf.nwt.ForumService.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuestionDTO {
    @NotBlank(message = "UID korisnika je obavezan.")
    private String userUid;
    @NotBlank(message = "Naslov pitanja je obavezan.")
    private String naslov;
    @NotBlank(message = "Sadržaj pitanja je obavezan.")
    private String sadrzaj;
    @NotNull(message = "Status anonimnosti je obavezan.")
    @DecimalMin(value = "0", message = "Status anonimnosti može biti ili 0 ili 1.")
    @DecimalMax(value = "1", message = "Status anonimnosti može biti ili 0 ili 1.")
    private Integer anonimnost;

    public QuestionDTO(String userUid, String naslov, String sadrzaj, Integer anonimnost) {
        this.userUid = userUid;
        this.naslov = naslov;
        this.sadrzaj = sadrzaj;
        this.anonimnost = anonimnost;
    }

    public QuestionDTO() {

    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
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
