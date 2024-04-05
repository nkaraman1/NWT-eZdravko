package ba.unsa.etf.nwt.PatientService.services;

import ba.unsa.etf.nwt.PatientService.DTO.TestDTO;
import ba.unsa.etf.nwt.PatientService.model.*;
import ba.unsa.etf.nwt.PatientService.repositories.TestRepository;
import ba.unsa.etf.nwt.PatientService.repositories.TestTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TestService {
    private final TestRepository testRepository;

    private final Validator validator;

    private final TestTypeRepository testTypeRepository;

    public TestService(TestRepository testRepository, Validator validator, TestTypeRepository testTypeRepository) {
        this.testRepository = testRepository;
        this.validator = validator;
        this.testTypeRepository = testTypeRepository;
    }

    public List<TestDTO> getTests(){
        List<Test> tests = testRepository.findAll();
        if (tests.isEmpty()) {
            return Collections.emptyList();
        }
        return tests.stream().map(this::convertToDTO).toList();
    }

    public ResponseEntity<?> addTest(TestDTO testDTO) {
        Errors errors = new BeanPropertyBindingResult(testDTO, "testDTO");
        validator.validate(testDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        TestType testType = testTypeRepository.findById(testDTO.getTip_nalaza_id()).orElse(null);
        if (testType == null) {
            return new ResponseEntity<>(new ErrorMsg("not found","Nije pronadjen nijedan tip nalaza sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        Test test = convertToEntity(testDTO, testType);
        test = testRepository.save(test);
        return new ResponseEntity<>(convertToDTO(test), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getTest(Long id) {
        ResponseEntity<?> response = getTestNotDTO(id);
        if(response.getStatusCode() != HttpStatus.OK){
            return response;
        }
        Test test = (Test) response.getBody();
        assert test!=null;
        return new ResponseEntity<>(convertToDTO(test), HttpStatus.OK);
    }

    private ResponseEntity<?> getTestNotDTO(Long id){
        Optional<Test> optionalTest = testRepository.findById(id);
        if (optionalTest.isEmpty()){
            return new ResponseEntity<>(new ErrorMsg("not found","Nije pronadjen nijedan nalaz sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        Test test = optionalTest.get();
        return new ResponseEntity<>(test, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteTest(Long id) {
        ResponseEntity<?> response = getTest(id);
        if(response.getStatusCode()==HttpStatus.OK){
            testRepository.deleteById(id);
        }
        return response;
    }

    public ResponseEntity<?> updateTest(Long id, TestDTO testDTO) {
        ResponseEntity<?> response = getTestNotDTO(id);
        if(response.getStatusCode() != HttpStatus.OK) {
            return response;
        }

        Errors errors = new BeanPropertyBindingResult(testDTO, "testDTO");
        validator.validate(testDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        TestType testType = testTypeRepository.findById(testDTO.getTip_nalaza_id()).orElse(null);
        if (testType == null) {
            return new ResponseEntity<>(new ErrorMsg("not found","Nije pronadjen nijedan tip nalaza sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        Test test = (Test) response.getBody();
        assert test != null;
        updateFromDTO(test, testDTO,testType);
        test = testRepository.save(test);
        return new ResponseEntity<>(convertToDTO(test), HttpStatus.OK);
    }

    private Test convertToEntity(TestDTO testDTO, TestType testType) {
        Test test = new Test();
        return updateFromDTO(test, testDTO, testType);
    }

    private Test updateFromDTO(Test test, TestDTO testDTO, TestType testType){
        test.setDijagnoza(testDTO.getDijagnoza());
        test.setDoktor_uid(testDTO.getDoktor_uid());
        test.setLaborant_uid(testDTO.getLaborant_uid());
        test.setPacijent_uid(testDTO.getPacijent_uid());
        test.setVrijeme_dijagnoze(testDTO .getVrijeme_dijagnoze());
        test.setVrijeme_pregleda(testDTO.getVrijeme_pregleda());
        test.setTip_nalaza(testType);
        return test;
    }

    private TestDTO convertToDTO(Test test){
        TestDTO testDTO =  new TestDTO(test.getPacijent_uid(),
                test.getLaborant_uid(),
                test.getDoktor_uid(),
                test.getTip_nalaza().getID(),
                test.getDijagnoza(),
                test.getVrijeme_pregleda(),
                test.getVrijeme_dijagnoze()
        );
        testDTO.setID(test.getID());
        testDTO.setRezultati(test.getRezultati().stream().map(TestResult::getID).toList());
        return testDTO;
    }

}
