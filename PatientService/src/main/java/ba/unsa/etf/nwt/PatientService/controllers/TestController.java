package ba.unsa.etf.nwt.PatientService.controllers;

import ba.unsa.etf.nwt.PatientService.DTO.ReferralDTO;
import ba.unsa.etf.nwt.PatientService.DTO.TestDTO;
import ba.unsa.etf.nwt.PatientService.model.Test;
import ba.unsa.etf.nwt.PatientService.repositories.TestRepository;
import ba.unsa.etf.nwt.PatientService.services.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/tests")
public class TestController {

    private final TestService testService;

    public TestController( TestService testService){
        this.testService =  testService;
    }

    @GetMapping(value="/")
    public List<TestDTO> getTests(){
        return testService.getTests();
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> addTest(@RequestBody TestDTO testDTO){
        return testService.addTest(testDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getTest(@PathVariable("id") Long id){
        return testService.getTest(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteTest(@PathVariable("id") Long id){
        return testService.deleteTest(id);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<?> updateTest(@PathVariable("id") Long id, @RequestBody TestDTO testDTO){
        return testService.updateTest(id, testDTO);
    }
}
