package ba.unsa.etf.nwt.PatientService;

import ba.unsa.etf.nwt.PatientService.model.DiaryEntry;
import ba.unsa.etf.nwt.PatientService.model.Examination;
import ba.unsa.etf.nwt.PatientService.model.Referral;
import ba.unsa.etf.nwt.PatientService.repositories.DiaryEntryRepository;
import ba.unsa.etf.nwt.PatientService.repositories.ExaminationRepository;
import ba.unsa.etf.nwt.PatientService.repositories.ReferralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class PatientServiceApplication {

	@Autowired
	private DiaryEntryRepository diaryEntryrepository;

	@Autowired
	private ExaminationRepository examinationRepository;

	@Autowired
	private ReferralRepository referralRepository;
	public static void main(String[] args) {
		SpringApplication.run(PatientServiceApplication.class, args);
	}

	@GetMapping(value="/diary-entries")
	public List<DiaryEntry> getDiaryEntries() {
		return diaryEntryrepository.findAll();
	}

	@GetMapping(value="/examinations")
	public List<Examination> getExaminations(){ return examinationRepository.findAll(); }

	@GetMapping(value="/referrals")
	public List<Referral> getReferrals(){ return referralRepository.findAll(); }

}
