package ba.unsa.etf.nwt.PatientService.controllers;

import ba.unsa.etf.nwt.PatientService.DTO.TestResultDTO;
import ba.unsa.etf.nwt.PatientService.model.TestResult;
import ba.unsa.etf.nwt.PatientService.repositories.TestResultRepository;
import ba.unsa.etf.nwt.PatientService.services.TestResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/test-results")
public class TestResultController {

    private final TestResultService testResultService;

    public TestResultController(TestResultService testResultService){
        this.testResultService = testResultService;
    }

    @GetMapping(value="/")
    public List<TestResult> getTestResults(){
        return testResultService.getTestResults();
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> addTestResult(TestResultDTO testResultDTO){
        return testResultService.addTestResult(testResultDTO);
    }
}
