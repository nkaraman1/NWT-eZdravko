package ba.unsa.etf.nwt.PatientService.services;

import ba.unsa.etf.nwt.PatientService.DTO.TestItemDTO;
import ba.unsa.etf.nwt.PatientService.model.ErrorMsg;
import ba.unsa.etf.nwt.PatientService.model.Test;
import ba.unsa.etf.nwt.PatientService.model.TestItem;
import ba.unsa.etf.nwt.PatientService.model.TestType;
import ba.unsa.etf.nwt.PatientService.repositories.TestItemRepository;
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
public class TestItemService {
    private final TestItemRepository testItemRepository;
    private final Validator validator;

    private final TestTypeRepository testTypeRepository;

    public TestItemService(TestItemRepository testItemRepository, Validator validator, TestTypeRepository testTypeRepository) {
        this.testItemRepository = testItemRepository;
        this.validator = validator;
        this.testTypeRepository = testTypeRepository;
    }

    public List<TestItem> getTestItems(){
        List<TestItem> testItems = testItemRepository.findAll();
        if (testItems.isEmpty()){
            return Collections.emptyList();
        }
        return testItems;
    }

    public ResponseEntity<?> addTestItem(TestItemDTO testItemDTO) {
        Errors errors = new BeanPropertyBindingResult(testItemDTO, "testItemDTO");
        validator.validate(testItemDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        TestType testType = testTypeRepository.findById(testItemDTO.getTip_nalaza_id()).orElse(null);
        if (testType == null) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen nijedan tip testa sa tim ID-em."), HttpStatus.FORBIDDEN);
        }

        TestItem testItem = convertToEntity(testItemDTO, testType);
        testItem = testItemRepository.save(testItem);
        return new ResponseEntity<>(testItem, HttpStatus.CREATED);
    }

    private TestItem convertToEntity(TestItemDTO testItemDTO, TestType testType) {
        TestItem testItem = new TestItem();
        testItem.setNaziv(testItemDTO.getNaziv());
        testItem.setMjerna_jedinica(testItemDTO.getMjerna_jedinica());
        testItem.setRef(testItemDTO.getRef());
        testItem.setRef_min(testItemDTO.getRef_min());
        testItem.setRef_max(testItemDTO.getRef_max());
        testItem.setTip_nalaza(testType);
        return testItem;
    }
}
