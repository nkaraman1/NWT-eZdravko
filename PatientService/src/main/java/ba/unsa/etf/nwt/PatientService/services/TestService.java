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

    public List<Test> getTests(){
        List<Test> tests = testRepository.findAll();
        if (tests.isEmpty()) {
            return Collections.emptyList();
        }
        return tests;
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
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen nijedan tip testa sa tim ID-em."), HttpStatus.FORBIDDEN);
        }

        Test test = convertToEntity(testDTO, testType);
        test = testRepository.save(test);
        return new ResponseEntity<>(test, HttpStatus.CREATED);
    }

    private Test convertToEntity(TestDTO testDTO, TestType testType) {
        Test test = new Test();
        test.setDijagnoza(testDTO.getDijagnoza());
        test.setDoktor_uid(testDTO.getDoktor_uid());
        test.setLaborant_uid(testDTO.getLaborant_uid());
        test.setPacijent_uid(testDTO.getPacijent_uid());
        test.setVrijeme_dijagnoze(testDTO .getVrijeme_dijagnoze());
        test.setVrijeme_pregleda(testDTO.getVrijeme_pregleda());
        test.setTip_nalaza(testType);
        return  test;
    }
}
