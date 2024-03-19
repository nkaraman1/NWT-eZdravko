package ba.unsa.etf.nwt.NewsService.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ProduzenjeTerapije")
public class Terapy {
    @Id
    private Long ID;
    @Column
    private String lijek;
    @Column
    private String napomena;
    @Column
    private Integer kolicina;
    //@ManyToOne
    //@JoinColumn(name = "pacijent_uid", referencedColumnName = "UID")
    @Column
    private String pacijent_uid;
    //@ManyToOne
    //@JoinColumn(name = "doktor_uid", referencedColumnName = "UID")
    @Column
    private String doktor_uid;

    public Terapy(Long ID, String lijek, String napomena, Integer kolicina, String pacijent_uid, String doktor_uid) {
        this.ID = ID;
        this.lijek = lijek;
        this.napomena = napomena;
        this.kolicina = kolicina;
        this.pacijent_uid = pacijent_uid;
        this.doktor_uid = doktor_uid;
    }

    public Terapy() {
        this.ID = null;
        this.lijek = null;
        this.napomena = null;
        this.kolicina = null;
        this.pacijent_uid = null;
        this.doktor_uid = null;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
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
