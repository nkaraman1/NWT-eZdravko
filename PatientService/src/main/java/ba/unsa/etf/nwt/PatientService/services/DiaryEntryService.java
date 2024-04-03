package ba.unsa.etf.nwt.PatientService.services;

import ba.unsa.etf.nwt.PatientService.DTO.DiaryEntryDTO;
import ba.unsa.etf.nwt.PatientService.controllers.DiaryEntryController;
import ba.unsa.etf.nwt.PatientService.model.DiaryEntry;
import ba.unsa.etf.nwt.PatientService.model.ErrorMsg;
import ba.unsa.etf.nwt.PatientService.repositories.DiaryEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DiaryEntryService {
    private final DiaryEntryRepository diaryEntryRepository;

    private final Validator validator;

    @Autowired
    public DiaryEntryService(DiaryEntryRepository diaryEntryRepository, Validator validator) {
        this.diaryEntryRepository = diaryEntryRepository;
        this.validator = validator;
    }

    public List<DiaryEntry> getDiaryEntries() {
        List<DiaryEntry> diaryEntries = diaryEntryRepository.findAll();
        if (diaryEntries.isEmpty()) {
            return Collections.emptyList();
        }
        return diaryEntries;
    }

    public ResponseEntity<?> addDiaryEntry(DiaryEntryDTO diaryEntryDTO){
        Errors errors = new BeanPropertyBindingResult(diaryEntryDTO, "diaryEntryDTO");
        validator.validate(diaryEntryDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg("validation",errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        DiaryEntry diaryEntry = convertToEntity(diaryEntryDTO);
        diaryEntry =  diaryEntryRepository.save(diaryEntry);
        return new ResponseEntity<>(diaryEntry, HttpStatus.CREATED);
    }


    public ResponseEntity<?> getDiaryEntry(Long id) {
        Optional<DiaryEntry> optionalDiaryEntry = diaryEntryRepository.findById(id);
        if (optionalDiaryEntry.isEmpty()){
            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronađen nijedan zapis u dnevniku sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        DiaryEntry diaryEntry = optionalDiaryEntry.get();
        return new ResponseEntity<>(diaryEntry, HttpStatus.OK);

    }

    public ResponseEntity<?> deleteDiaryEntry(Long id) {
        Optional<DiaryEntry> optionalDiaryEntry = diaryEntryRepository.findById(id);
        if (optionalDiaryEntry.isEmpty()){
            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronađen nijedan zapis u dnevniku sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        DiaryEntry diaryEntry = optionalDiaryEntry.get();

        diaryEntryRepository.deleteById(id);

        return new ResponseEntity<>(diaryEntry, HttpStatus.OK);
    }


    public ResponseEntity<?> updateDiaryEntry(Long id, DiaryEntryDTO diaryEntryDTO){
        Optional<DiaryEntry> optionalDiaryEntry = diaryEntryRepository.findById(id);
        if (optionalDiaryEntry.isEmpty()){
            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronađen nijedan zapis u dnevniku sa tim ID-em."), HttpStatus.NOT_FOUND);
        }


        Errors errors = new BeanPropertyBindingResult(diaryEntryDTO, "diaryEntryDTO");
        validator.validate(diaryEntryDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg("validation",errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        DiaryEntry diaryEntry = optionalDiaryEntry.get();
        diaryEntry = updateFromEntity(diaryEntry, diaryEntryDTO);
        diaryEntry = diaryEntryRepository.save(diaryEntry);

        return new ResponseEntity<>(diaryEntry, HttpStatus.OK);
    }

    private DiaryEntry updateFromEntity(DiaryEntry diaryEntry, DiaryEntryDTO diaryEntryDTO) {
        diaryEntry.setBmi(diaryEntryDTO.getBmi());
        diaryEntry.setDatum(diaryEntryDTO.getDatum());
        diaryEntry.setPuls(diaryEntryDTO.getPuls());
        diaryEntry.setTezina(diaryEntryDTO.getTezina());
        diaryEntry.setBroj_koraka(diaryEntryDTO.getBroj_koraka());
        diaryEntry.setUnos_vode(diaryEntryDTO.getUnos_vode());
        diaryEntry.setVisina(diaryEntryDTO.getVisina());
        diaryEntry.setUser_uid(diaryEntryDTO.getUser_uid());
        return diaryEntry;
    }

    private DiaryEntry convertToEntity(DiaryEntryDTO diaryEntryDTO){
        DiaryEntry diaryEntry = new DiaryEntry();
        return updateFromEntity(diaryEntry, diaryEntryDTO);
    }


}
