package ba.unsa.etf.nwt.ForumService.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import ba.unsa.etf.nwt.UserManagementService.model.User;

import java.util.List;

@Entity
@Data
@Table(name = "Pitanja")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column
    private String user_uid;
    @Column
    private String naslov;
    @Column
    private String sadrzaj;
    @Column
    private Integer anonimnost;

    @OneToMany(mappedBy = "pitanje", cascade = CascadeType.ALL)
    private List<Comment> comments;
    @JsonManagedReference
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Question(Long ID, String user_uid, String naslov, String sadrzaj, Integer anonimnost) {
        this.user_uid = user_uid;
        this.naslov = naslov;
        this.sadrzaj = sadrzaj;
        this.anonimnost = anonimnost;
    }

    public Question() {
        this.user_uid = "";
        this.naslov = "";
        this.sadrzaj = "";
        this.anonimnost = 0;
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

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
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
