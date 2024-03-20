package ba.unsa.etf.nwt.PatientService.controllers;

import ba.unsa.etf.nwt.PatientService.model.TestResult;
import ba.unsa.etf.nwt.PatientService.repositories.TestResultRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class TestResultController {

    private final TestResultRepository testResultRepository;

    public TestResultController(TestResultRepository testResultRepository){
        this.testResultRepository = testResultRepository;
    }

    @GetMapping(value="/test-results")
    public List<TestResult> getTestResults(){
        List<TestResult> testResults = testResultRepository.findAll();
        if (testResults.isEmpty()){
            return Collections.emptyList();
        }
        return testResults;
    }
}
