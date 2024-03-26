package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.DTO.SurveyQuestionDTO;
import ba.unsa.etf.nwt.SurveyService.model.ErrorMsg;
import ba.unsa.etf.nwt.SurveyService.model.Survey;
import ba.unsa.etf.nwt.SurveyService.model.SurveyQuestion;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyQuestionRepository;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SurveyQuestionController {
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyRepository surveyRepository;
    private final Validator validator;


    public SurveyQuestionController(SurveyQuestionRepository surveyQuestionRepository, SurveyRepository surveyRepository, Validator validator){
        this.surveyQuestionRepository = surveyQuestionRepository;
        this.surveyRepository = surveyRepository;
        this.validator = validator;
    }

    @GetMapping(value="/surveyquestions")
    public List<SurveyQuestion> getSurveyQuestions() {
        return surveyQuestionRepository.findAll();
    }

    @PostMapping(value="/surveyquestions")
    public ResponseEntity<?> createSurveyQuestion(@RequestBody SurveyQuestionDTO surveyQuestionDTO){
        Errors errors = new BeanPropertyBindingResult(surveyQuestionDTO, "surveyQuestionDTO");
        validator.validate(surveyQuestionDTO, errors);

        if(errors.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        Survey survey = surveyRepository.findById(Math.toIntExact(surveyQuestionDTO.getSurveyId())).orElse(null);
        if(survey == null) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena nijedna anketa sa tim ID-em."), HttpStatus.FORBIDDEN);
        }

        SurveyQuestion surveyQuestion = new SurveyQuestion();
        surveyQuestion.setSadrzaj(surveyQuestionDTO.getSadrzaj());

        SurveyQuestion savedSurveyQuestion = surveyQuestionRepository.save(surveyQuestion);
        return new ResponseEntity<>(savedSurveyQuestion, HttpStatus.CREATED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
