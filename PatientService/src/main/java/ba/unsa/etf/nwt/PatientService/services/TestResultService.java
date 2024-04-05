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
import java.util.Objects;
import java.util.Optional;

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

    public List<TestResultDTO> getTestResults(){
        List<TestResult> testResults = testResultRepository.findAll();
        if (testResults.isEmpty()){
            return Collections.emptyList();
        }
        return testResults.stream().map(this::convertToDTO).toList();
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
            return new ResponseEntity<>(new ErrorMsg("not found","Nije pronadjen nijedan nalaz sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        TestItem testItem = testItemRepository.findById(testResultDTO.getStavka_id()).orElse(null);
        if (testItem == null) {
            return new ResponseEntity<>(new ErrorMsg("not found","Nije pronadjena nijedna stavka nalaza sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        if (!Objects.equals(test.getTip_nalaza().getID(), testItem.getTip_nalaza().getID())){
            return new ResponseEntity<>(new ErrorMsg("conflicted arguments","Stavka i nalaz nemaju isti tip nalaza."), HttpStatus.CONFLICT);
        }

        TestResult testResult = convertToEntity(testResultDTO, test, testItem);
        testResult = testResultRepository.save(testResult);

        return new ResponseEntity<>(convertToDTO(testResult), HttpStatus.CREATED);

    }


    public ResponseEntity<?> getTestResult(Long id) {
        ResponseEntity<?> response = getTestResultNotDTO(id);
        if(response.getStatusCode()!=HttpStatus.OK){
            return response;
        }
        TestResult testResult = (TestResult) response.getBody();
        assert testResult!=null;
        return new ResponseEntity<>(convertToDTO(testResult), HttpStatus.OK);
    }

    public ResponseEntity<?> getTestResultNotDTO(Long id){
        Optional<TestResult> optionalTestResult = testResultRepository.findById(id);
        if (optionalTestResult.isEmpty()){
            return new ResponseEntity<>(new ErrorMsg("not found","Nije pronadjen nijedan rezultat sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        TestResult testResult = optionalTestResult.get();
        return new ResponseEntity<>(testResult, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteTestResult(Long id) {
        ResponseEntity<?> response = getTestResult(id);
        if(response.getStatusCode()==HttpStatus.OK){
            testResultRepository.deleteById(id);
        }
        return response;
    }

    public ResponseEntity<?> updateTestResult(Long id, TestResultDTO testResultDTO) {
        ResponseEntity<?> response = getTestResultNotDTO(id);
        if(response.getStatusCode()!=HttpStatus.OK){
            return response;
        }

        Errors errors = new BeanPropertyBindingResult(testResultDTO, "testResultDTO");
        validator.validate(testResultDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        Test test = testRepository.findById(testResultDTO.getNalaz_id()).orElse(null);
        if (test == null) {
            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronadjen nijedan nalaz sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        TestItem testItem = testItemRepository.findById(testResultDTO.getStavka_id()).orElse(null);
        if (testItem == null) {
            return new ResponseEntity<>(new ErrorMsg("not found","Nije pronadjena nijedna stavka nalaza sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        if (!Objects.equals(test.getTip_nalaza().getID(), testItem.getTip_nalaza().getID())){
            return new ResponseEntity<>(new ErrorMsg("conflicted arguments","Stavka i nalaz nemaju isti tip nalaza."), HttpStatus.CONFLICT);
        }

        TestResult testResult = (TestResult) response.getBody();
        assert testResult!=null;
        updateFromDTO(testResult, testResultDTO, test, testItem);
        testResult = testResultRepository.save(testResult);
        return new ResponseEntity<>(convertToDTO(testResult), HttpStatus.OK);
    }



    private TestResult convertToEntity(TestResultDTO testResultDTO, Test test, TestItem testItem) {
        return updateFromDTO(new TestResult(), testResultDTO, test, testItem);
    }

    private TestResult updateFromDTO(TestResult testResult, TestResultDTO testResultDTO, Test test, TestItem testItem){
        testResult.setNalaz(test);
        testResult.setStavka(testItem);
        testResult.setVrijednost(testResultDTO.getVrijednost());
        return  testResult;
    }

    private TestResultDTO convertToDTO(TestResult testResult){
        TestResultDTO testResultDTO = new TestResultDTO(
                testResult.getVrijednost(),
                testResult.getNalaz().getID(),
                testResult.getStavka().getID()
        );
        testResultDTO.setID(testResult.getID());
        return testResultDTO;
    }
}
