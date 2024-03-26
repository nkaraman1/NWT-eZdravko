package ba.unsa.etf.nwt.PatientService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name="nalaz_rezultati")
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne
    @JoinColumn(name = "nalaz_id", referencedColumnName = "ID")
    @NotBlank(message = "ID nalaza je obavezan.")
    private Test nalaz_id;

    @ManyToOne
    @JoinColumn(name = "stavka_id", referencedColumnName = "ID")
    @NotBlank(message = "ID stavke je obavezan.")
    private TestItem stavka_id;

    @Column
    @NotBlank(message = "Vrijednost je obavezna.")
    private String vrijednost;
}
