package ba.unsa.etf.nwt.UserManagementService.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @Column
    private String nazivRole;

    public Role() {
        this.ID = null;
        this.nazivRole = null;
    }

    public Role(Long ID, String nazivRole) {
        this.ID = ID;
        this.nazivRole = nazivRole;
    }

    public Role(String nazivRole) {
        this.nazivRole = nazivRole;
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

    @Override
    public String toString() {
        return nazivRole;
    }
}
