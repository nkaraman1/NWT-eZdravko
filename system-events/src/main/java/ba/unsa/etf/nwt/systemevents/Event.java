package ba.unsa.etf.nwt.systemevents;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    private String timestamp;
    private String ime_servisa;
    private Long user_id;
    private String akcija;
    private String resurs;
    private Boolean uspjesna_akcija;

    public Event() {
    }

    public Event(Long ID, String timestamp, String ime_servisa, Long user_id, String akcija, String resurs, Boolean uspjesna_akcija) {
        this.ID = ID;
        this.timestamp = timestamp;
        this.ime_servisa = ime_servisa;
        this.user_id = user_id;
        this.akcija = akcija;
        this.resurs = resurs;
        this.uspjesna_akcija = uspjesna_akcija;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIme_servisa() {
        return ime_servisa;
    }

    public void setIme_servisa(String ime_servisa) {
        this.ime_servisa = ime_servisa;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getAkcija() {
        return akcija;
    }

    public void setAkcija(String akcija) {
        this.akcija = akcija;
    }

    public String getResurs() {
        return resurs;
    }

    public void setResurs(String resurs) {
        this.resurs = resurs;
    }

    public Boolean getUspjesna_akcija() {
        return uspjesna_akcija;
    }

    public void setUspjesna_akcija(Boolean uspjesna_akcija) {
        this.uspjesna_akcija = uspjesna_akcija;
    }

    public void setProperties(EventRequest eventRequest) {
        this.setAkcija(eventRequest.getAkcija());
        this.setIme_servisa(eventRequest.getImeServisa());
        this.setResurs(eventRequest.getResurs());
        this.setUser_id(eventRequest.getUserId());
        this.setTimestamp(eventRequest.getTimestamp());
        this.setUspjesna_akcija(eventRequest.getUspjesnaAkcija());
    }
}
