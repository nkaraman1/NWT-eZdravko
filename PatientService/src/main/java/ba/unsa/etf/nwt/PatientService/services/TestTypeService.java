package ba.unsa.etf.nwt.PatientService.services;

import ba.unsa.etf.nwt.PatientService.DTO.TestTypeDTO;
import ba.unsa.etf.nwt.PatientService.model.ErrorMsg;
import ba.unsa.etf.nwt.PatientService.model.Test;
import ba.unsa.etf.nwt.PatientService.model.TestItem;
import ba.unsa.etf.nwt.PatientService.model.TestType;
import ba.unsa.etf.nwt.PatientService.repositories.TestTypeRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TestTypeService {
    private final TestTypeRepository testTypeRepository;

    private final Validator validator;

    public TestTypeService(TestTypeRepository testTypeRepository, Validator validator) {
        this.testTypeRepository = testTypeRepository;
        this.validator = validator;
    }

    public List<TestTypeDTO> getTestTypes(){
        List<TestType> testTypes =  testTypeRepository.findAll();
        if (testTypes.isEmpty()){
            return Collections.emptyList();
        }
        return testTypes.stream().map(this::convertToDTO).toList();
    }

    public ResponseEntity<?> addTestType(TestTypeDTO testTypeDTO) {
        Errors errors = new BeanPropertyBindingResult(testTypeDTO, "testTypeDTO");
        validator.validate(testTypeDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        TestType testType = convertToEntity(testTypeDTO);
        testType = testTypeRepository.save(testType);
        return new ResponseEntity<>(convertToDTO(testType), HttpStatus.CREATED);
    }


    public ResponseEntity<?> getTestType(Long id) {
        ResponseEntity<?> response = getTestTypeNotDTO(id);
        if(response.getStatusCode()!=HttpStatus.OK){
            return response;
        }
        TestType testType = (TestType) response.getBody();
        assert testType!=null;
        return new ResponseEntity<>(convertToDTO(testType), HttpStatus.OK);
    }

    private ResponseEntity<?> getTestTypeNotDTO(Long id) {
        Optional<TestType> optionalTestType = testTypeRepository.findById(id);
        if (optionalTestType.isEmpty()){
            return new ResponseEntity<>(new ErrorMsg("not found","Nije pronadjen nijedan tip nalaza sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        TestType testType = optionalTestType.get();
        return new ResponseEntity<>(testType, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteTestType(Long id) {
        ResponseEntity<?> response = getTestType(id);
        if(response.getStatusCode()==HttpStatus.OK){
            testTypeRepository.deleteById(id);
        }
        return response;
    }

    public ResponseEntity<?> updateTestType(Long id, TestTypeDTO testTypeDTO) {
        ResponseEntity<?> response = getTestTypeNotDTO(id);
        if(response.getStatusCode()!=HttpStatus.OK){
            return response;
        }
        Errors errors = new BeanPropertyBindingResult(testTypeDTO, "testTypeDTO");
        validator.validate(testTypeDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        TestType testType = (TestType) response.getBody();
        assert testType!=null;
        updateFromDTO(testType, testTypeDTO);
        testType = testTypeRepository.save(testType);
        return new ResponseEntity<>(convertToDTO(testType), HttpStatus.OK);
    }

    private TestType convertToEntity(TestTypeDTO testTypeDTO) {
        return updateFromDTO(new TestType(),testTypeDTO);
    }

    private TestType updateFromDTO(TestType testType, TestTypeDTO testTypeDTO){
        testType.setNaziv(testTypeDTO.getNaziv());
        return testType;
    }

    private TestTypeDTO convertToDTO(TestType testType){
        TestTypeDTO testTypeDTO = new TestTypeDTO(testType.getNaziv());
        if(testType.getNalazi()!=null){
            testTypeDTO.setNalazi(testType.getNalazi().stream().map(Test::getID).toList());
        }
        if(testType.getStavke()!=null){
            testTypeDTO.setStavke(testType.getStavke().stream().map(TestItem::getID).toList());
        }
        testTypeDTO.setID(testType.getID());
        return testTypeDTO;
    }

}
