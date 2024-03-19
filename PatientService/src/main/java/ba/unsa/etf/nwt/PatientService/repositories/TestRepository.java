package ba.unsa.etf.nwt.PatientService.repositories;

import ba.unsa.etf.nwt.PatientService.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Integer> {
}
