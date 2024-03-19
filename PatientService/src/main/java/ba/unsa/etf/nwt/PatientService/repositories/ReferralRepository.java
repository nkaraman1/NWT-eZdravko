package ba.unsa.etf.nwt.PatientService.repositories;

import ba.unsa.etf.nwt.PatientService.model.Referral;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferralRepository extends JpaRepository<Referral, Integer> {
}
