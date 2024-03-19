package ba.unsa.etf.nwt.PatientService.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="nalaz_rezultati")
public class TestResult {
    @Id
    private Long ID;

    @ManyToOne
    @JoinColumn(name = "nalaz_id", referencedColumnName = "ID")
    private Test nalaz_id;

    @ManyToOne
    @JoinColumn(name = "stavka_id", referencedColumnName = "ID")
    private TestItem stavka_id;

    @Column
    private String vrijednost;
}
