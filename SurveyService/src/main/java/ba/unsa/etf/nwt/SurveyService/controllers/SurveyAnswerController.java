package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.DTO.AnswerOptionsDTO;
import ba.unsa.etf.nwt.SurveyService.DTO.SurveyAnswerDTO;
import ba.unsa.etf.nwt.SurveyService.DTO.SurveyQuestionDTO;
import ba.unsa.etf.nwt.SurveyService.model.*;
import ba.unsa.etf.nwt.SurveyService.repositories.AnswerOptionsRepository;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyAnswerRepository;
import ba.unsa.etf.nwt.SurveyService.services.SurveyAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class SurveyAnswerController {

    private final SurveyAnswerService surveyAnswerService;

    @Autowired
    public SurveyAnswerController(SurveyAnswerService surveyAnswerService) {
        this.surveyAnswerService = surveyAnswerService;
    }

    @GetMapping(value="/surveyanswers")
    public List<SurveyAnswerDTO> getSurveyAnswers() {
        return surveyAnswerService.getSurveyAnswers();
    }

    @PostMapping(value="/surveyanswers")
    public ResponseEntity<?> createSurveyAnswer(@RequestBody SurveyAnswerDTO surveyAnswerDTO){
        return surveyAnswerService.createSurveyAnswer(surveyAnswerDTO);
    }

    @GetMapping(value = "/surveyanswers/{id}")
    public ResponseEntity<?> getSurveyAnswerByID(@PathVariable Long id) {
        return surveyAnswerService.getSurveyAnswerByID(id);
    }

//    @GetMapping(value = "/surveyanswers/answer-id/{answerId}")
//    public ResponseEntity<?> getSurveyAnswerByAnswerOptionsID(@PathVariable Long answerId) {
//        return surveyAnswerService.getSurveyAnswerByAnswerOptionsID(answerId);
//    }

    @DeleteMapping(value = "/surveyanswers/{id}")
    public ResponseEntity<?> deleteSurveyAnswer(@PathVariable Long id) {
        return surveyAnswerService.deleteSurveyAnswer(id);
    }

    @PutMapping(value="/surveyanswers/{id}")
    public ResponseEntity<?> updateSurveyAnswer(@PathVariable Long id, @RequestBody SurveyAnswerDTO surveyAnswerDTO){
        return surveyAnswerService.updateSurveyAnswer(id, surveyAnswerDTO);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}