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
    public List<DiaryEntry> getDiaryEntries() {
        return diaryEntryService.getDiaryEntries();
    }


}
