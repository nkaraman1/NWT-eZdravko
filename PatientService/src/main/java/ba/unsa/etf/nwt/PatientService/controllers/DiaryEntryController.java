package ba.unsa.etf.nwt.PatientService.controllers;

import ba.unsa.etf.nwt.PatientService.model.DiaryEntry;
import ba.unsa.etf.nwt.PatientService.repositories.DiaryEntryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class DiaryEntryController {
    private final DiaryEntryRepository diaryEntryRepository;

    public DiaryEntryController(DiaryEntryRepository diaryEntryRepository) {
        this.diaryEntryRepository = diaryEntryRepository;
    }

    @GetMapping(value="/diary-entries")
    public List<DiaryEntry> getDiaryEntries() {
        List<DiaryEntry> diaryEntries = diaryEntryRepository.findAll();
        if (diaryEntries.isEmpty()) {
            return Collections.emptyList();
        }
        return diaryEntries;
    }
}
