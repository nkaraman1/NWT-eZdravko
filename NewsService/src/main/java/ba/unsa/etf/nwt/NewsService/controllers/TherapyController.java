package ba.unsa.etf.nwt.NewsService.controllers;

import ba.unsa.etf.nwt.NewsService.DTO.NotificationDTO;
import ba.unsa.etf.nwt.NewsService.DTO.TherapyDTO;
import ba.unsa.etf.nwt.NewsService.model.ErrorMsg;
import ba.unsa.etf.nwt.NewsService.model.Notification;
import ba.unsa.etf.nwt.NewsService.model.Therapy;
import ba.unsa.etf.nwt.NewsService.repositories.TherapyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class TherapyController {
    private final TherapyRepository therapyRepository;
    private final Validator validator;

    public TherapyController(TherapyRepository therapyRepository, Validator validator) {
        this.therapyRepository = therapyRepository;
        this.validator = validator;
    }

    @GetMapping(value="/therapy")
    public List<Therapy> getTherapies() {
        List<Therapy> therapy = therapyRepository.findAll();
        if (therapy.isEmpty()) {
            return Collections.emptyList();
        }
        return therapy;
    }

    @PostMapping(value="/therapy")
    public ResponseEntity<?> createTherapy(@RequestBody TherapyDTO therapyDTO) {
        Errors errors = new BeanPropertyBindingResult(therapyDTO, "therapyDTO");
        validator.validate(therapyDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
//            throw new RuntimeException(errorMessage.toString());
        }
        Therapy therapy = new Therapy();
        therapy.setLijek(therapyDTO.getLijek());
        therapy.setNapomena(therapyDTO.getNapomena());
        therapy.setKolicina(therapyDTO.getKolicina());
        therapy.setPacijent_uid(therapyDTO.getPacijent_uid());
        therapy.setDoktor_uid(therapyDTO.getDoktor_uid());

        Therapy savedTherapy = therapyRepository.save(therapy);
        return new ResponseEntity<>(savedTherapy, HttpStatus.CREATED);
    }

    @GetMapping(value = "/therapy/{id}")
    public ResponseEntity<?> getTherapyByID(@PathVariable Long id) {
        Optional<Therapy> therapy = therapyRepository.findById(id);
        if (therapy.isPresent()) {
            return new ResponseEntity<>(therapy.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena terapija sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/therapy/{id}")
    public ResponseEntity<?> deleteTherapy(@PathVariable Long id) {
        Optional<Therapy> therapy = therapyRepository.findById(id);
        if (therapy.isPresent()) {
            therapyRepository.deleteById(id);
            return new ResponseEntity<>(therapy.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena terapija sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value="/therapy/{id}")
    public ResponseEntity<?> updateTherapy(@PathVariable Long id, @RequestBody TherapyDTO therapyDTO){
        Optional<Therapy> optionalTherapy = therapyRepository.findById(id);
        if (!optionalTherapy.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena terapija sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        Errors errors = new BeanPropertyBindingResult(therapyDTO, "therapyDTO");
        validator.validate(therapyDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
//            throw new RuntimeException(errorMessage.toString());
        }

        Therapy existingTherapy = optionalTherapy.get();

        existingTherapy.setDoktor_uid(therapyDTO.getDoktor_uid());
        existingTherapy.setPacijent_uid(therapyDTO.getPacijent_uid());
        existingTherapy.setKolicina(therapyDTO.getKolicina());
        existingTherapy.setNapomena(therapyDTO.getNapomena());
        existingTherapy.setLijek(therapyDTO.getLijek());

        Therapy updatedTherapy = therapyRepository.save(existingTherapy);

        return new ResponseEntity<>(updatedTherapy, HttpStatus.OK);
    }

    @PatchMapping(value="/therapy/{id}")
    public ResponseEntity<?> patchTherapy(@PathVariable Long id, @RequestBody TherapyDTO therapyDTO) {
        Optional<Therapy> optionalTherapy = therapyRepository.findById(id);
        if (!optionalTherapy.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena terapija sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

//        Errors errors = new BeanPropertyBindingResult(therapyDTO, "therapyDTO");
//        validator.validate(therapyDTO, errors);
//
//        if (errors.hasErrors()) {
//            StringBuilder errorMessage = new StringBuilder();
//            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
//            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
////            throw new RuntimeException(errorMessage.toString());
//        }

        Therapy existingTherapy = optionalTherapy.get();

        if(therapyDTO.getDoktor_uid() != null){
            existingTherapy.setDoktor_uid(therapyDTO.getDoktor_uid());
        }
        if(therapyDTO.getPacijent_uid() != null){
            existingTherapy.setPacijent_uid(therapyDTO.getPacijent_uid());
        }
        if(therapyDTO.getKolicina() != null){
            existingTherapy.setKolicina(therapyDTO.getKolicina());
        }
        if(therapyDTO.getNapomena() != null){
            existingTherapy.setNapomena(therapyDTO.getNapomena());
        }
        if(therapyDTO.getLijek() != null){
            existingTherapy.setLijek(therapyDTO.getLijek());
        }

        Therapy updatedTherapy = therapyRepository.save(existingTherapy);

        return new ResponseEntity<>(updatedTherapy, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
