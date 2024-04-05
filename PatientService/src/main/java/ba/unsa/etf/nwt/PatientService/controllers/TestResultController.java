package ba.unsa.etf.nwt.PatientService.controllers;

import ba.unsa.etf.nwt.PatientService.DTO.TestResultDTO;
import ba.unsa.etf.nwt.PatientService.DTO.TestTypeDTO;
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
    public List<TestResultDTO> getTestResults(){
        return testResultService.getTestResults();
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> addTestResult(TestResultDTO testResultDTO){
        return testResultService.addTestResult(testResultDTO);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<?> getTestResult(@PathVariable("id") Long id){
        return testResultService.getTestResult(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteTestResult(@PathVariable("id") Long id){
        return testResultService.deleteTestResult(id);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<?> updateTestResult(@PathVariable("id") Long id, @RequestBody TestResultDTO testResultDTO){
        return testResultService.updateTestResult(id, testResultDTO);
    }
}
