package ba.unsa.etf.nwt.UserManagementService.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Role")
public class Role {
    @Id
    private Long ID;
    @Column
    private String naziv_role;

    public Role() {
        this.ID = null;
        this.naziv_role = null;
    }

    public Role(Long ID, String naziv_role) {
        this.ID = ID;
        this.naziv_role = naziv_role;
    }

    public Role(String naziv_role) {
        this.naziv_role = naziv_role;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getNaziv_role() {
        return naziv_role;
    }

    public void setNaziv_role(String naziv_role) {
        this.naziv_role = naziv_role;
    }

    @Override
    public String toString() {
        return naziv_role;
    }
}
