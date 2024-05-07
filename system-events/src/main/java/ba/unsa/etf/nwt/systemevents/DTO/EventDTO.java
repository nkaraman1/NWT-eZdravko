package ba.unsa.etf.nwt.systemevents.DTO;

import ba.unsa.etf.nwt.systemevents.model.Event;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public class EventDTO {
    private LocalDateTime timestamp;
    private String imeServisa;
    private Long userID;
    @Enumerated(EnumType.STRING)
    private Event.TipAkcije akcija;
    private String resurs;
    private Boolean uspjesnaAkcija;

    public EventDTO() {
    }

    public EventDTO(String imeServisa, Long userID, Event.TipAkcije akcija, String resurs, Boolean uspjesnaAkcija) {
        this.timestamp = LocalDateTime.now();
        this.imeServisa = imeServisa;
        this.userID = userID;
        this.akcija = akcija;
        this.resurs = resurs;
        this.uspjesnaAkcija = uspjesnaAkcija;
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

    public Event.TipAkcije getAkcija() {
        return akcija;
    }

    public void setAkcija(Event.TipAkcije akcija) {
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
