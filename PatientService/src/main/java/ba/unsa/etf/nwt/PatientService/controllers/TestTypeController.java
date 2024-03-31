package ba.unsa.etf.nwt.PatientService.controllers;

import ba.unsa.etf.nwt.PatientService.DTO.TestTypeDTO;
import ba.unsa.etf.nwt.PatientService.model.TestType;
import ba.unsa.etf.nwt.PatientService.repositories.TestTypeRepository;
import ba.unsa.etf.nwt.PatientService.services.TestTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value="/test-types")
public class TestTypeController {
    private final TestTypeService testTypeService;

    public TestTypeController(TestTypeService testTypeService) {
        this.testTypeService = testTypeService;
    }

    @GetMapping(value="/")
    public List<TestType> getTestTypes(){
       return testTypeService.getTestTypes();
    }

    @PostMapping(value="/")
    public ResponseEntity<?> addTestType(@RequestBody TestTypeDTO testTypeDTO){
        return testTypeService.addTestType(testTypeDTO);
    }
}
