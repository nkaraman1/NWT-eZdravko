package ba.unsa.etf.nwt.NewsService.DTO;

import jakarta.persistence.Column;

public class TherapyDTO {
    private String lijek;
    private String napomena;
    private Integer kolicina;
    private String pacijent_uid;
    private String doktor_uid;

    public TherapyDTO(String lijek, String napomena, Integer kolicina, String pacijent_uid, String doktor_uid) {
        this.lijek = lijek;
        this.napomena = napomena;
        this.kolicina = kolicina;
        this.pacijent_uid = pacijent_uid;
        this.doktor_uid = doktor_uid;
    }

    public TherapyDTO() {
    }

    public String getLijek() {
        return lijek;
    }

    public void setLijek(String lijek) {
        this.lijek = lijek;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public Integer getKolicina() {
        return kolicina;
    }

    public void setKolicina(Integer kolicina) {
        this.kolicina = kolicina;
    }

    public String getPacijent_uid() {
        return pacijent_uid;
    }

    public void setPacijent_uid(String pacijent_uid) {
        this.pacijent_uid = pacijent_uid;
    }

    public String getDoktor_uid() {
        return doktor_uid;
    }

    public void setDoktor_uid(String doktor_uid) {
        this.doktor_uid = doktor_uid;
    }
}
