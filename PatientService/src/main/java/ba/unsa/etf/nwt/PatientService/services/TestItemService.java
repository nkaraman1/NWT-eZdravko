package ba.unsa.etf.nwt.PatientService.services;

import ba.unsa.etf.nwt.PatientService.DTO.TestItemDTO;
import ba.unsa.etf.nwt.PatientService.model.*;
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
import java.util.Optional;

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

    public List<TestItemDTO> getTestItems(){
        List<TestItem> testItems = testItemRepository.findAll();
        if (testItems.isEmpty()){
            return Collections.emptyList();
        }
        return testItems.stream().map(this::convertToDTO).toList();
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
            return new ResponseEntity<>(new ErrorMsg("not found","Nije pronadjen nijedan tip nalaza sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        TestItem testItem = convertToEntity(testItemDTO, testType);
        testItem = testItemRepository.save(testItem);
        return new ResponseEntity<>(convertToDTO(testItem), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getTestItem(Long id) {
        ResponseEntity<?> response = getTestItemNotDTO(id);
        if(response.getStatusCode()!=HttpStatus.OK){
            return response;
        }
        TestItem testItem = (TestItem) response.getBody();
        assert testItem != null;
        return new ResponseEntity<>(convertToDTO(testItem), HttpStatus.OK);
    }

    private ResponseEntity<?> getTestItemNotDTO(Long id){
        Optional<TestItem> optionalTestItem = testItemRepository.findById(id);
        if (optionalTestItem.isEmpty()){
            return new ResponseEntity<>(new ErrorMsg("not found","Nije pronadjen nijedan nalaz sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        TestItem testItem = optionalTestItem.get();
        return new ResponseEntity<>(testItem, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteTestItem(Long id) {
        ResponseEntity<?> response = getTestItem(id);
        if(response.getStatusCode() == HttpStatus.OK){
            testItemRepository.deleteById(id);
        }
        return response;
    }

    public ResponseEntity<?> updateTestItem(Long id, TestItemDTO testItemDTO) {
        ResponseEntity<?> response = getTestItemNotDTO(id);
        if(response.getStatusCode() != HttpStatus.OK) {
            return response;
        }
        Errors errors = new BeanPropertyBindingResult(testItemDTO, "testItemDTO");
        validator.validate(testItemDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        TestType testType = testTypeRepository.findById(testItemDTO.getTip_nalaza_id()).orElse(null);
        if (testType == null) {
            return new ResponseEntity<>(new ErrorMsg("not found","Nije pronadjen nijedan tip nalaza sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        TestItem testItem = (TestItem) response.getBody();
        assert testItem != null;
        updateFromDTO(testItem, testItemDTO, testType);
        testItem = testItemRepository.save(testItem);
        return new ResponseEntity<>(convertToDTO(testItem), HttpStatus.OK);
    }

    private TestItem convertToEntity(TestItemDTO testItemDTO, TestType testType) {
        TestItem testItem = new TestItem();
        return updateFromDTO(testItem, testItemDTO,testType);
    }

    private TestItem updateFromDTO(TestItem testItem, TestItemDTO testItemDTO, TestType testType){
        testItem.setNaziv(testItemDTO.getNaziv());
        testItem.setMjerna_jedinica(testItemDTO.getMjerna_jedinica());
        testItem.setRef(testItemDTO.getRef());
        testItem.setRef_min(testItemDTO.getRef_min());
        testItem.setRef_max(testItemDTO.getRef_max());
        testItem.setTip_nalaza(testType);
        return testItem;
    }

    private TestItemDTO convertToDTO(TestItem testItem){
        TestItemDTO testItemDTO = new TestItemDTO(
                testItem.getNaziv(),
                testItem.getRef_min(),
                testItem.getRef_max(),
                testItem.getRef(),
                testItem.getMjerna_jedinica(),
                testItem.getTip_nalaza().getID()
        );
        if(!testItem.getRezultati().isEmpty()) {
            testItemDTO.setRezultati(testItem.getRezultati().stream().map(TestResult::getID).toList());
        }
        testItemDTO.setID(testItem.getID());
        return testItemDTO;
    }


}
