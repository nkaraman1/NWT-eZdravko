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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
