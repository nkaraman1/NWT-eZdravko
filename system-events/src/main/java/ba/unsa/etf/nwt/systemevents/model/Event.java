package ba.unsa.etf.nwt.systemevents.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Events")
public class Event {
    @Getter
    public enum TipAkcije {
        CREATE, DELETE, GET, UPDATE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column
    private LocalDateTime timestamp;

    @Column
    private String imeServisa;

    @Column
    private Long userID;

    @Column
    @Enumerated(EnumType.STRING)
    private TipAkcije akcija;

    @Column
    private String resurs;

    @Column
    private Boolean uspjesnaAkcija;

    public Event(Long ID, LocalDateTime timestamp, String imeServisa, Long userID, TipAkcije akcija, String resurs, Boolean uspjesnaAkcija) {
        this.ID = ID;
        this.timestamp = timestamp;
        this.imeServisa = imeServisa;
        this.userID = userID;
        this.akcija = akcija;
        this.resurs = resurs;
        this.uspjesnaAkcija = uspjesnaAkcija;
    }

    public Event() {
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getImeServisa() {
        return imeServisa;
    }

    public void setImeServisa(String imeServisa) {
        this.imeServisa = imeServisa;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public TipAkcije getAkcija() {
        return akcija;
    }

    public void setAkcija(TipAkcije akcija) {
        this.akcija = akcija;
    }

    public String getResurs() {
        return resurs;
    }

    public void setResurs(String resurs) {
        this.resurs = resurs;
    }

    public Boolean getUspjesnaAkcija() {
        return uspjesnaAkcija;
    }

    public void setUspjesnaAkcija(Boolean uspjesnaAkcija) {
        this.uspjesnaAkcija = uspjesnaAkcija;
    }
}
