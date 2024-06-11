package ba.unsa.etf.nwt.ForumService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Random;

@Entity
@Data
@Table(name = "Role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "naziv_role")
    @NotBlank(message = "Naziv role je obavezan!")
    private String nazivRole;

    @Column(name = "potreban_kod")
    private boolean potrebanKod;

    @Column(name = "kod")
    private String kod;

    public static String generisiKod() {
        String alphaNumeric = "0123456789qwertzuiopasdfghjklyxcvbnm";
        StringBuilder kod = new StringBuilder(10);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(alphaNumeric.length());
            kod.append(alphaNumeric.charAt(index));
        }
        return kod.toString();
    }

    public Role() {

    }

    public Role(Long ID, String nazivRole, boolean potrebanKod, String kod) {
        this.ID = ID;
        this.nazivRole = nazivRole;
        this.potrebanKod = potrebanKod;
        this.kod = kod;
    }

    public Role(Long ID, String nazivRole, boolean potrebanKod) {
        this.ID = ID;
        this.nazivRole = nazivRole;
        this.potrebanKod = potrebanKod;
        if (potrebanKod) {
            this.kod = generisiKod();
        }
        else {
            this.kod = null;
        }
    }

    public Role(String nazivRole, boolean potrebanKod) {
        this.nazivRole = nazivRole;
        this.potrebanKod = potrebanKod;
        if (potrebanKod) {
            this.kod = generisiKod();
        }
        else {
            this.kod = null;
        }
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getNazivRole() {
        return nazivRole;
    }

    public void setNazivRole(String nazivRole) {
        this.nazivRole = nazivRole;
    }

    public boolean isPotrebanKod() {
        return potrebanKod;
    }

    public void setPotrebanKod(boolean potrebanKod) {
        this.potrebanKod = potrebanKod;
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    @Override
    public String toString() {
        return nazivRole;
    }
}
