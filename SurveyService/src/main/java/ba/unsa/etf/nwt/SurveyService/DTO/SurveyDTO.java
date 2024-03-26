package ba.unsa.etf.nwt.SurveyService.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SurveyDTO {
    @NotBlank(message = "UID korisnika je obavezan.")
    private String userUid;

    @NotBlank(message = "Naslov ankete je obavezan.")
    private String naslov;

    @NotBlank(message = "Opis ankete je obavezan.")
    private String opis;

    @NotNull(message = "Status (aktivnosti) ankete je obavezan.")
    @DecimalMin(value = "0", message = "Status (aktivnosti) ankete može biti ili 0 ili 1.")
    @DecimalMax(value = "1", message = "Status (aktivnosti) ankete može biti ili 0 ili 1.")
    private Integer status;

    public SurveyDTO(String userUid, String naslov, String opis, Integer status) {
        this.userUid = userUid;
        this.naslov = naslov;
        this.opis = opis;
        this.status = status;
    }

    public SurveyDTO() {
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
}
