package ba.unsa.etf.nwt.PatientService.controllers;

import ba.unsa.etf.nwt.PatientService.model.Test;
import ba.unsa.etf.nwt.PatientService.repositories.TestRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class TestController {

    private final TestRepository testRepository;

    public TestController( TestRepository testRepository){
        this.testRepository =  testRepository;
    }

    @GetMapping(value="/tests")
    public List<Test> getTests(){
        List<Test> tests = testRepository.findAll();
        if (tests.isEmpty()) {
            return Collections.emptyList();
        }
        return tests;
    }
}
