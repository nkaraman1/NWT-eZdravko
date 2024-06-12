package ba.unsa.etf.nwt.NewsService.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Notifikacije")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @Column
    private String tip_notifikacije;
    @Column
    private String sadrzaj;
    @Column
    private String uid;

    public Notification(Long ID, String tip_notifikacije, String sadrzaj, String uid) {
        this.ID = ID;
        this.tip_notifikacije = tip_notifikacije;
        this.sadrzaj = sadrzaj;
        this.uid = uid;
    }

    public Notification() {
        this.ID = null;
        this.tip_notifikacije = null;
        this.sadrzaj = null;
        this.uid = null;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
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
