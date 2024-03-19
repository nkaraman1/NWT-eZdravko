package ba.unsa.etf.nwt.NewsService.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Novosti")
public class News {
    @Id
    private Long ID;
    @Column
    private String naslov;
    @Column
    private String sadrzaj;
    @Column
    private String slika;
    //@ManyToOne
    //@JoinColumn(name = "user_uid", referencedColumnName = "UID")
    @Column
    private String user_uid;

    public News(Long ID, String naslov, String sadrzaj, String slika, String user_uid) {
        this.ID = ID;
        this.naslov = naslov;
        this.sadrzaj = sadrzaj;
        this.slika = slika;
        this.user_uid = user_uid;
    }

    public News() {
        this.ID = null;
        this.naslov = null;
        this.sadrzaj = null;
        this.slika = null;
        this.user_uid = null;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
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