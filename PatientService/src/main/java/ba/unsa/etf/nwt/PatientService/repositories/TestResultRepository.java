package ba.unsa.etf.nwt.PatientService.repositories;

import ba.unsa.etf.nwt.PatientService.model.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestResultRepository extends JpaRepository<TestResult, Integer> {
}
