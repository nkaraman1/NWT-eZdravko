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
    public List<Test> getTests(){
        return testService.getTests();
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> addTest(@RequestBody TestDTO testDTO){
        return testService.addTest(testDTO);
    }
}
