package ba.unsa.etf.nwt.NewsService.DTO;

import jakarta.validation.constraints.NotBlank;

public class NotificationDTO {
    @NotBlank(message = "Tip notifikacije je obavezan.")
    private String tip_notifikacije;
    @NotBlank(message = "Sadržaj notifikacije je obavezan.")
    private String sadrzaj;
    @NotBlank(message = "UID korisnika je obavezan.")
    private String uid;

    public NotificationDTO(String tip_notifikacije, String sadrzaj, String uid) {
        this.tip_notifikacije = tip_notifikacije;
        this.sadrzaj = sadrzaj;
        this.uid = uid;
    }

    public NotificationDTO() {
    }

    public String getTip_notifikacije() {
        return tip_notifikacije;
    }

    public void setTip_notifikacije(String tip_notifikacije) {
        this.tip_notifikacije = tip_notifikacije;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
