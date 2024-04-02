package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.DTO.SurveyDTO;
import ba.unsa.etf.nwt.SurveyService.model.ErrorMsg;
import ba.unsa.etf.nwt.SurveyService.model.Survey;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyRepository;
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
public class SurveyController {
    private final SurveyRepository surveyRepository;
    private final Validator validator;


    public SurveyController(SurveyRepository surveyRepository, Validator validator){
        this.surveyRepository = surveyRepository;
        this.validator = validator;
    }

    @GetMapping(value="/surveys")
    public @ResponseBody List<Survey> getSurveys(){
        List<Survey> surveys = surveyRepository.findAll();
        if (surveys.isEmpty()) {
            // If no surveys found, return an empty list instead of null
            return Collections.emptyList();
        }
        return surveys;
    }

    @PostMapping(value="/surveys")
    public ResponseEntity<?> createSurvey(@RequestBody SurveyDTO surveyDTO){
        Errors errors = new BeanPropertyBindingResult(surveyDTO, "surveyDTO");
        validator.validate(surveyDTO, errors);

        if(errors.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }
        Survey survey = new Survey();
        survey.setUser_uid(surveyDTO.getUserUid());
        survey.setNaslov(surveyDTO.getNaslov());
        survey.setOpis(surveyDTO.getOpis());
        survey.setStatus(surveyDTO.getStatus());

        Survey savedSurvey = surveyRepository.save(survey);
        return new ResponseEntity<>(savedSurvey, HttpStatus.CREATED);
    }

    @GetMapping(value = "/surveys/survey/id/{id}")
    public ResponseEntity<?> getSurveyByID(@PathVariable Long id) {
        Optional<Survey> survey = surveyRepository.findById(id);
        if (survey.isPresent()) {
            return new ResponseEntity<>(survey.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena anketa sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/surveys/survey/naslov/{naslov}")
    public ResponseEntity<?> getSurveyByNaslov(@PathVariable String naslov) {
        Optional<Survey> survey = surveyRepository.findByNaslov(naslov);
        if (survey.isPresent()) {
            return new ResponseEntity<>(survey.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena anketa sa tim naslovom."), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/surveys/survey/status/{status}")
    public ResponseEntity<?> getSurveyByStatus(@PathVariable Integer status) {
        List<Survey> survey = surveyRepository.findByStatus(status);
        if (survey.isEmpty()) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(survey, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/surveys/survey/{id}")
    public ResponseEntity<?> deleteSurvey(@PathVariable Long id) {
        Optional<Survey> survey = surveyRepository.findById(id);
        if (survey.isPresent()) {
            surveyRepository.deleteById(id);
            return new ResponseEntity<>(survey.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena anketa sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value="/surveys/survey/{id}")
    public ResponseEntity<?> updateSurvey(@PathVariable Long id, @RequestBody SurveyDTO surveyDTO){
        Optional<Survey> optionalSurvey = surveyRepository.findById(id);
        if (!optionalSurvey.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena nijedna anketa sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        Errors errors = new BeanPropertyBindingResult(surveyDTO, "surveyDTO");
        validator.validate(surveyDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
//            throw new RuntimeException(errorMessage.toString());
        }

        Survey existingSurvey = optionalSurvey.get();

        existingSurvey.setUser_uid(surveyDTO.getUserUid());
        existingSurvey.setNaslov(surveyDTO.getNaslov());
        existingSurvey.setOpis(surveyDTO.getOpis());
        existingSurvey.setStatus(surveyDTO.getStatus());

        Survey updatedSurvey = surveyRepository.save(existingSurvey);

        return new ResponseEntity<>(updatedSurvey, HttpStatus.OK);
    }

    @PutMapping(value="/surveys/{id}/user-uid/{userUid}")
    public ResponseEntity<?> updateUserUid(@PathVariable Long id, @PathVariable String userUid) {
        Optional<Survey> optionalSurvey = surveyRepository.findById(id);
        if (optionalSurvey.isPresent()) {
            Survey survey = optionalSurvey.get();
            survey.setUser_uid(userUid);
            surveyRepository.save(survey);
            return ResponseEntity.ok(survey);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Neispravni parametri!"), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value="/surveys/{id}/naslov/{naslov}")
    public ResponseEntity<?> updateNaslov(@PathVariable Long id, @PathVariable String naslov) {
        Optional<Survey> optionalSurvey = surveyRepository.findById(id);
        if (optionalSurvey.isPresent()) {
            Survey survey = optionalSurvey.get();
            survey.setNaslov(naslov);
            surveyRepository.save(survey);
            return ResponseEntity.ok(survey);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Neispravni parametri!"), HttpStatus.NOT_FOUND);        }
    }

    @PutMapping(value="/surveys/{id}/opis/{opis}")
    public ResponseEntity<?> updateOpis(@PathVariable Long id, @PathVariable String opis) {
        Optional<Survey> optionalSurvey = surveyRepository.findById(id);
        if (optionalSurvey.isPresent()) {
            Survey survey = optionalSurvey.get();
            survey.setOpis(opis);
            surveyRepository.save(survey);
            return ResponseEntity.ok(survey);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Neispravni parametri!"), HttpStatus.NOT_FOUND);        }
    }
    @PutMapping(value="/surveys/{id}/status/{status}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        Optional<Survey> optionalSurvey = surveyRepository.findById(id);
        if (optionalSurvey.isPresent()) {
            Survey survey = optionalSurvey.get();
            survey.setStatus(status);
            surveyRepository.save(survey);
            return ResponseEntity.ok(survey);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Neispravni parametri!"), HttpStatus.NOT_FOUND);        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}