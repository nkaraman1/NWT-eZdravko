package ba.unsa.etf.nwt.PatientService.services;

import ba.unsa.etf.nwt.PatientService.DTO.TestTypeDTO;
import ba.unsa.etf.nwt.PatientService.model.ErrorMsg;
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

@Service
public class TestTypeService {
    private final TestTypeRepository testTypeRepository;

    private final Validator validator;

    public TestTypeService(TestTypeRepository testTypeRepository, Validator validator) {
        this.testTypeRepository = testTypeRepository;
        this.validator = validator;
    }

    public List<TestType> getTestTypes(){
        List<TestType> testTypes =  testTypeRepository.findAll();
        if (testTypes.isEmpty()){
            return Collections.emptyList();
        }
        return testTypes;
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
        return new ResponseEntity<>(testType, HttpStatus.CREATED);

    }

    private TestType convertToEntity(@NotNull TestTypeDTO testTypeDTO) {
        return new TestType(testTypeDTO.getNaziv());
    }
}
