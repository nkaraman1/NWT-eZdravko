package ba.unsa.etf.nwt.NewsService.controllers;

import ba.unsa.etf.nwt.NewsService.DTO.NotificationDTO;
import ba.unsa.etf.nwt.NewsService.DTO.TherapyDTO;
import ba.unsa.etf.nwt.NewsService.model.Notification;
import ba.unsa.etf.nwt.NewsService.model.Therapy;
import ba.unsa.etf.nwt.NewsService.repositories.TherapyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Therapy> createTherapy(@RequestBody TherapyDTO therapyDTO) {
        Errors errors = new BeanPropertyBindingResult(therapyDTO, "therapyDTO");
        validator.validate(therapyDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            throw new RuntimeException(errorMessage.toString());
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
}
