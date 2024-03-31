package ba.unsa.etf.nwt.PatientService.controllers;

import ba.unsa.etf.nwt.PatientService.DTO.TestItemDTO;
import ba.unsa.etf.nwt.PatientService.model.TestItem;
import ba.unsa.etf.nwt.PatientService.repositories.TestItemRepository;
import ba.unsa.etf.nwt.PatientService.services.TestItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value="/test-items")
public class TestItemController {

    private final TestItemService testItemService;

    public TestItemController(TestItemService testItemService){
        this.testItemService = testItemService;
    }

    @GetMapping(value="/")
    public List<TestItem> getTestItems(){
        List<TestItem> testItems = testItemService.getTestItems();
        if (testItems.isEmpty()){
            return Collections.emptyList();
        }
        return testItems;
    }

    @PostMapping(value="/")
    public ResponseEntity<?> addTestItem(@RequestBody TestItemDTO testItemDTO){
        return testItemService.addTestItem(testItemDTO);
    }
}
