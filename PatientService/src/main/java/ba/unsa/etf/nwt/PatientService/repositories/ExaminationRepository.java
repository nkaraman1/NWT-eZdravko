package ba.unsa.etf.nwt.PatientService.repositories;

import ba.unsa.etf.nwt.PatientService.model.Examination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExaminationRepository extends JpaRepository<Examination, Integer> {
}
