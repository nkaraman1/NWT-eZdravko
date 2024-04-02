package ba.unsa.etf.nwt.PatientService.repositories;
import ba.unsa.etf.nwt.PatientService.model.DiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DiaryEntryRepository extends  JpaRepository<DiaryEntry, Long>{
}