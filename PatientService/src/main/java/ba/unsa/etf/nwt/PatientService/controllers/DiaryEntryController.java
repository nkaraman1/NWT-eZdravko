package ba.unsa.etf.nwt.PatientService.controllers;

import ba.unsa.etf.nwt.PatientService.DTO.DiaryEntryDTO;
import ba.unsa.etf.nwt.PatientService.model.DiaryEntry;
import ba.unsa.etf.nwt.PatientService.repositories.DiaryEntryRepository;
import ba.unsa.etf.nwt.PatientService.services.DiaryEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/diary-entries")
public class DiaryEntryController {

    private final DiaryEntryService diaryEntryService;

    @Autowired
    public DiaryEntryController(DiaryEntryService diaryEntryService) {
        this.diaryEntryService = diaryEntryService;
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> addDiaryEntry(@RequestBody DiaryEntryDTO diaryEntryDTO){
        return diaryEntryService.addDiaryEntry(diaryEntryDTO);
    }

    @GetMapping(value="/")
    public List<DiaryEntryDTO> getDiaryEntries() {
        return diaryEntryService.getDiaryEntries();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getDiaryEntry(@PathVariable("id") Long id){
        return diaryEntryService.getDiaryEntry(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteDiaryEntry(@PathVariable("id") Long id){
        return diaryEntryService.deleteDiaryEntry(id);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateDiaryEntry(@PathVariable("id") Long id, @RequestBody DiaryEntryDTO diaryEntryDTO){
        return diaryEntryService.updateDiaryEntry(id, diaryEntryDTO);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateDiaryEntryPartial(@PathVariable("id") Long id, @RequestBody Map<String, Object> fields){
        return diaryEntryService.updateDiaryEntryPartial(id, fields);
    }


}
