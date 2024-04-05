package ba.unsa.etf.nwt.PatientService.controllers;

import ba.unsa.etf.nwt.PatientService.DTO.TestDTO;
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
    public List<TestItemDTO> getTestItems(){
        return testItemService.getTestItems();
    }

    @PostMapping(value="/")
    public ResponseEntity<?> addTestItem(@RequestBody TestItemDTO testItemDTO){
        return testItemService.addTestItem(testItemDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getTestItem(@PathVariable("id") Long id){
        return testItemService.getTestItem(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteTestItem(@PathVariable("id") Long id){
        return testItemService.deleteTestItem(id);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<?> updateTestItem(@PathVariable("id") Long id, @RequestBody TestItemDTO testItemDTO){
        return testItemService.updateTestItem(id, testItemDTO);
    }
}
