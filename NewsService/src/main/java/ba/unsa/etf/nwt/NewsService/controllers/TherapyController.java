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

@RestController
public class TherapyController {
    private final TherapyRepository therapyRepository;
    private final Validator validator;

    public TherapyController(TherapyRepository therapyRepository, Validator validator) {
        this.therapyRepository = therapyRepository;
        this.validator = validator;
    }

    @GetMapping(value="/therapy")
    public List<Therapy> getQuestions() {
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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
