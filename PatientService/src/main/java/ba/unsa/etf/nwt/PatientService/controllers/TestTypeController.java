package ba.unsa.etf.nwt.PatientService.controllers;

import ba.unsa.etf.nwt.PatientService.model.TestType;
import ba.unsa.etf.nwt.PatientService.repositories.TestTypeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class TestTypeController {
    private final TestTypeRepository testTypeRepository;

    public TestTypeController(TestTypeRepository testTypeRepository) {
        this.testTypeRepository = testTypeRepository;
    }

    @GetMapping(value="/test-types")
    public List<TestType> getTestTypes(){
        List<TestType> testTypes =  testTypeRepository.findAll();
        if (testTypes.isEmpty()){
            return Collections.emptyList();
        }
        return testTypes;
    }
}
