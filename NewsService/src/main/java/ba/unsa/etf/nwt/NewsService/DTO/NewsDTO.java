package ba.unsa.etf.nwt.NewsService.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public class NewsDTO {
    @NotBlank(message = "Naslov je obavezan.")
    private String naslov;
    @NotBlank(message = "Sadržaj novosti je obavezan.")
    private String sadrzaj;
    @NotBlank(message = "Slika je obavezna.")
    private String slika;
    @NotBlank(message = "UID korisnika je obavezan.")
    private String user_uid;

    public NewsDTO(String naslov, String sadrzaj, String slika, String user_uid) {
        this.naslov = naslov;
        this.sadrzaj = sadrzaj;
        this.slika = slika;
        this.user_uid = user_uid;
    }

    public NewsDTO() {
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

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }
}
