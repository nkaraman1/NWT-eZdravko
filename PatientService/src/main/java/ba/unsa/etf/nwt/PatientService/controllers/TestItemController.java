package ba.unsa.etf.nwt.PatientService.controllers;

import ba.unsa.etf.nwt.PatientService.model.TestItem;
import ba.unsa.etf.nwt.PatientService.repositories.TestItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class TestItemController {

    private final TestItemRepository testItemRepository;

    public TestItemController(TestItemRepository testItemRepository){
        this.testItemRepository = testItemRepository;
    }

    @GetMapping(value="/test-items")
    public List<TestItem> getTestItems(){
        List<TestItem> testItems = testItemRepository.findAll();
        if (testItems.isEmpty()){
            return Collections.emptyList();
        }
        return testItems;
    }
}
