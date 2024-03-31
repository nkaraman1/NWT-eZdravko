package ba.unsa.etf.nwt.PatientService.repositories;

import ba.unsa.etf.nwt.PatientService.model.TestType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestTypeRepository extends JpaRepository<TestType, Long> {
}
