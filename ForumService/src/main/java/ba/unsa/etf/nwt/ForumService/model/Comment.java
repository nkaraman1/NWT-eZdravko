package ba.unsa.etf.nwt.ForumService.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import ba.unsa.etf.nwt.UserManagementService.model.User;

@Entity
@Data
@Table(name = "Komentari")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "ID")
    private Question pitanje;
    //@ManyToOne
    //@JoinColumn(name = "user_uid", referencedColumnName = "UID")
    @Column
    private String user_uid;
    @Column
    private String sadrzaj;
    @Column
    private Integer anonimnost;

    public Comment(Long ID, Question pitanje, String user_id, String sadrzaj, Integer anonimnost) {
        this.ID = ID;
        this.pitanje = pitanje;
        this.user_uid = user_id;
        this.sadrzaj = sadrzaj;
        this.anonimnost = anonimnost;
    }

    public Comment() {
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
    @JsonBackReference
    public Question getPitanje() {
        return pitanje;
    }

    public void setPitanje(Question pitanje) {
        this.pitanje = pitanje;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String UID_Korisnik) {
        this.user_uid = UID_Korisnik;
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
