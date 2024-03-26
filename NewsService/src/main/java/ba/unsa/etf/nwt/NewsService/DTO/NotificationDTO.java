package ba.unsa.etf.nwt.NewsService.DTO;

import jakarta.persistence.Column;

public class NotificationDTO {
    private String tip_notifikacije;
    private String sadrzaj;
    private String user_uid;

    public NotificationDTO(String tip_notifikacije, String sadrzaj, String user_uid) {
        this.tip_notifikacije = tip_notifikacije;
        this.sadrzaj = sadrzaj;
        this.user_uid = user_uid;
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

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }
}
