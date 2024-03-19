package ba.unsa.etf.nwt.ForumService.model;

import jakarta.persistence.*;
import lombok.Data;
import ba.unsa.etf.nwt.UserManagementService.model.User;

@Entity
@Data
@Table(name = "Pitanja")
public class Question {
    @Id
    private Long ID;
    // @ManyToOne(targetEntity = ba.unsa.etf.nwt.UserManagementService.model.User.class)
    // @JoinColumn(name = "user_uid", referencedColumnName = "UID")
    // private User user_uid;
    @Column
    private String user_uid;
    @Column
    private String naslov;
    @Column
    private String sadrzaj;
    @Column
    private Integer anonimnost;


    public Question(Long ID, String UID_Korisnik, String naslov, String sadrzaj, Integer anonimnost) {
        this.ID = ID;
        this.user_uid = UID_Korisnik;
        this.naslov = naslov;
        this.sadrzaj = sadrzaj;
        this.anonimnost = anonimnost;
    }

    public Question() {
        this.ID = null;
        this.user_uid = null;
        naslov = null;
        sadrzaj = null;
        anonimnost = null;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String UID_Korisnik) {
        this.user_uid = UID_Korisnik;
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
