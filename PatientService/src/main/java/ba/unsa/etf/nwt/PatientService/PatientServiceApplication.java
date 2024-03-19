package ba.unsa.etf.nwt.PatientService;

import ba.unsa.etf.nwt.PatientService.model.*;
import ba.unsa.etf.nwt.PatientService.repositories.*;
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
	@Autowired
	private TestRepository testRepository;
	@Autowired
	private TestItemRepository testItemRepository;
	@Autowired
	private TestTypeRepository testTypeRepository;
	@Autowired
	private TestResultRepository testResultRepository;
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

	@GetMapping(value="/tests")
	public List<Test> getTests(){ return testRepository.findAll(); }

	@GetMapping(value="/test-results")
	public List<TestResult> getTestResults(){ return testResultRepository.findAll(); }

	@GetMapping(value="/test-types")
	public List<TestType> getTestTypes(){ return testTypeRepository.findAll(); }

	@GetMapping(value="/test-items")
	public List<TestItem> getTestItems(){ return testItemRepository.findAll(); }

}
