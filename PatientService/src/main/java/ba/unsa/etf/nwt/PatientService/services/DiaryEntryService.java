package ba.unsa.etf.nwt.PatientService.services;

import ba.unsa.etf.nwt.PatientService.DTO.DiaryEntryDTO;
import ba.unsa.etf.nwt.PatientService.model.DiaryEntry;
import ba.unsa.etf.nwt.PatientService.model.ErrorMsg;
import ba.unsa.etf.nwt.PatientService.repositories.DiaryEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class DiaryEntryService {
    private final DiaryEntryRepository diaryEntryRepository;

    private final Validator validator;

    @Autowired
    public DiaryEntryService(DiaryEntryRepository diaryEntryRepository, Validator validator) {
        this.diaryEntryRepository = diaryEntryRepository;
        this.validator = validator;
    }

    public List<DiaryEntryDTO> getDiaryEntries() {
        List<DiaryEntry> diaryEntries = diaryEntryRepository.findAll();
        if (diaryEntries.isEmpty()) {
            return Collections.emptyList();
        }
        return diaryEntries.stream().map(this::convertToDTO).toList();
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
        return new ResponseEntity<>(convertToDTO(diaryEntry), HttpStatus.CREATED);
    }


    public ResponseEntity<?> getDiaryEntry(Long id) {
        ResponseEntity<?> response = getDiaryEntryNotDTO(id);
        if(response.getStatusCode() != HttpStatus.OK){
            return response;
        }
        DiaryEntry diaryEntry = (DiaryEntry) response.getBody();
        assert diaryEntry != null;
        DiaryEntryDTO diaryEntryDTO = convertToDTO(diaryEntry);
        return new ResponseEntity<>(diaryEntryDTO, HttpStatus.OK);

    }

    public List<DiaryEntry> getDiaryEntryByUID(String uid) {
        List<DiaryEntry> diaryEntries = diaryEntryRepository.findAll();
        if (diaryEntries.isEmpty()) {
            return Collections.emptyList();
        }
        return diaryEntries.stream()
                .filter(entry -> entry.getUser_uid().equals(uid))
                .sorted(Comparator.comparingLong(DiaryEntry::getID).reversed())
                .collect(Collectors.toList());
    }

    private ResponseEntity<?> getDiaryEntryNotDTO(Long id){
        Optional<DiaryEntry> optionalDiaryEntry = diaryEntryRepository.findById(id);
        if (optionalDiaryEntry.isEmpty()){
            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronađen nijedan zapis u dnevniku sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        DiaryEntry diaryEntry = optionalDiaryEntry.get();
        return new ResponseEntity<>(diaryEntry, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteDiaryEntry(Long id) {
        ResponseEntity<?> response = getDiaryEntry(id);
        if(response.getStatusCode() == HttpStatus.OK){
            diaryEntryRepository.deleteById(id);
        }
        return response;
    }


    public ResponseEntity<?> updateDiaryEntry(Long id, DiaryEntryDTO diaryEntryDTO){
        ResponseEntity<?> response = getDiaryEntryNotDTO(id);
        if(response.getStatusCode() != HttpStatus.OK){
            return response;
        }
        DiaryEntry diaryEntry = (DiaryEntry) response.getBody();

        Errors errors = new BeanPropertyBindingResult(diaryEntryDTO, "diaryEntryDTO");
        validator.validate(diaryEntryDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg("validation",errorMessage.toString()), HttpStatus.FORBIDDEN);
        }
;
        assert diaryEntry != null;
        updateFromDTO(diaryEntry, diaryEntryDTO);
        diaryEntry = diaryEntryRepository.save(diaryEntry);

        return new ResponseEntity<>(convertToDTO(diaryEntry), HttpStatus.OK);
    }

    public ResponseEntity<?> updateDiaryEntryPartial(Long id, Map<String, Object> fields) {
        Optional<DiaryEntry> optionalDiaryEntry = diaryEntryRepository.findById(id);
        if (optionalDiaryEntry.isEmpty()){
            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronađen nijedan zapis u dnevniku sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        AtomicBoolean error = new AtomicBoolean(false);
        StringBuilder errorMessage = new StringBuilder();
        fields.forEach((key, value)->{
            Field field = ReflectionUtils.findField(DiaryEntry.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, optionalDiaryEntry.get(), value);
            }else{
                error.set(true);
                errorMessage.append("Dnevnik nema polje ").append(key).append(". ");
            }
        });
        if(error.get()){
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }
        DiaryEntry diaryEntry = diaryEntryRepository.save(optionalDiaryEntry.get());
        return new ResponseEntity<>(convertToDTO(diaryEntry), HttpStatus.OK);
    }

    private DiaryEntry updateFromDTO(DiaryEntry diaryEntry, DiaryEntryDTO diaryEntryDTO) {
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
        return updateFromDTO(diaryEntry, diaryEntryDTO);
    }

    private DiaryEntryDTO convertToDTO(DiaryEntry diaryEntry){
        DiaryEntryDTO diaryEntryDTO = new DiaryEntryDTO(
                diaryEntry.getUser_uid(),
                diaryEntry.getDatum(),
                diaryEntry.getVisina(),
                diaryEntry.getTezina(),
                diaryEntry.getBmi(),
                diaryEntry.getPuls(),
                diaryEntry.getUnos_vode(),
                diaryEntry.getBroj_koraka()
        );

        diaryEntryDTO.setID(diaryEntry.getID());
        return diaryEntryDTO;
    }


}
