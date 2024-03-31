package ba.unsa.etf.nwt.PatientService.services;

import ba.unsa.etf.nwt.PatientService.DTO.TestResultDTO;
import ba.unsa.etf.nwt.PatientService.DTO.TestTypeDTO;
import ba.unsa.etf.nwt.PatientService.model.*;
import ba.unsa.etf.nwt.PatientService.repositories.TestItemRepository;
import ba.unsa.etf.nwt.PatientService.repositories.TestRepository;
import ba.unsa.etf.nwt.PatientService.repositories.TestResultRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Collections;
import java.util.List;

@Service
public class TestResultService {

    private final TestResultRepository testResultRepository;

    private final Validator validator;

    private final TestItemRepository testItemRepository;
    private final TestRepository testRepository;

    public TestResultService(TestResultRepository testResultRepository, Validator validator, TestItemRepository testItemRepository, TestRepository testRepository) {
        this.testResultRepository = testResultRepository;
        this.validator = validator;
        this.testItemRepository = testItemRepository;
        this.testRepository = testRepository;
    }

    public List<TestResult> getTestResults(){
        List<TestResult> testResults = testResultRepository.findAll();
        if (testResults.isEmpty()){
            return Collections.emptyList();
        }
        return testResults;
    }

    public ResponseEntity<?> addTestResult(TestResultDTO testResultDTO) {
        Errors errors = new BeanPropertyBindingResult(testResultDTO, "testResultDTO");
        validator.validate(testResultDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        Test test = testRepository.findById(testResultDTO.getNalaz_id()).orElse(null);
        if (test == null) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen nijedan nalaz sa tim ID-em."), HttpStatus.FORBIDDEN);
        }

        TestItem testItem = testItemRepository.findById(testResultDTO.getStavka_id()).orElse(null);
        if (testItem == null) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena nijedna stavka nalaza sa tim ID-em."), HttpStatus.FORBIDDEN);
        }

        TestResult testResult = convertToEntity(testResultDTO, test, testItem);
        testResult = testResultRepository.save(testResult);
        return new ResponseEntity<>(testResult, HttpStatus.CREATED);

    }

    private TestResult convertToEntity(TestResultDTO testResultDTO, Test test, TestItem testItem) {
        return new TestResult(test, testItem, testResultDTO.getVrijednost());
    }

}
