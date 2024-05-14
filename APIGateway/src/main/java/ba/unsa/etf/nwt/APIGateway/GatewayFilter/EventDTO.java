package ba.unsa.etf.nwt.APIGateway.GatewayFilter;

import java.time.LocalDateTime;

public class EventDTO {
    private String timestamp;
    private String imeServisa;
    private Long userID;
    private String akcija;
    private String resurs;
    private Boolean uspjesnaAkcija;

    public EventDTO() {
    }

    public EventDTO(String imeServisa, Long userID, String akcija, String resurs, Boolean uspjesnaAkcija) {
        this.timestamp = LocalDateTime.now().toString();
        this.imeServisa = imeServisa;
        this.userID = userID;
        this.akcija = akcija;
        this.resurs = resurs;
        this.uspjesnaAkcija = uspjesnaAkcija;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
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

    public Boolean getUspjesnaAkcija() {
        return uspjesnaAkcija;
    }

    public void setUspjesnaAkcija(Boolean uspjesnaAkcija) {
        this.uspjesnaAkcija = uspjesnaAkcija;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "timestamp=" + timestamp +
                ", imeServisa='" + imeServisa + '\'' +
                ", userID=" + userID +
                ", akcija=" + akcija +
                ", resurs='" + resurs + '\'' +
                ", uspjesnaAkcija=" + uspjesnaAkcija +
                '}';
    }
}