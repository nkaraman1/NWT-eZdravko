package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.DTO.SurveyQuestionDTO;
import ba.unsa.etf.nwt.SurveyService.model.ErrorMsg;
import ba.unsa.etf.nwt.SurveyService.model.Survey;
import ba.unsa.etf.nwt.SurveyService.model.SurveyQuestion;
import ba.unsa.etf.nwt.SurveyService.services.SurveyQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
public class SurveyQuestionController {
    private final SurveyQuestionService surveyQuestionService;

    @Autowired
    public SurveyQuestionController(SurveyQuestionService surveyQuestionService) {
        this.surveyQuestionService = surveyQuestionService;
    }

    @GetMapping(value="/surveyquestions")
    public List<SurveyQuestionDTO> getSurveyQuestions() {
        return surveyQuestionService.getSurveyQuestions();
    }

    @PostMapping(value="/surveyquestions")
    public ResponseEntity<?> createSurveyQuestion(@RequestBody SurveyQuestionDTO surveyQuestionDTO){
        return surveyQuestionService.createSurveyQuestion(surveyQuestionDTO);
    }

    @GetMapping(value = "/surveyquestions/id/{id}")
    public ResponseEntity<?> getSurveyQuestionByID(@PathVariable Long id) {
        return surveyQuestionService.getSurveyQuestionByID(id);
    }

//    @GetMapping(value = "/surveyquestions/survey-id/{surveyId}")
//    public ResponseEntity<?> getSurveyQuestionBySurveyID(@PathVariable Long surveyId) {
//        return surveyQuestionService.getSurveyQuestionBySurveyID(surveyId);
//    }

    @DeleteMapping(value = "/surveyquestions/{id}")
    public ResponseEntity<?> deleteSurveyQuestion(@PathVariable Long id) {
        return surveyQuestionService.deleteSurveyQuestion(id);
    }

    @PutMapping(value="/surveyquestions/{id}")
    public ResponseEntity<?> updateSurveyQuestion(@PathVariable Long id, @RequestBody SurveyQuestionDTO surveyQuestionDTO){
        return surveyQuestionService.updateSurveyQuestion(id, surveyQuestionDTO);
    }

    @PatchMapping(value = "/surveyquestions/{id}")
    public ResponseEntity<?> updateSurveyQuestionPartial(@PathVariable("id") Long id, @RequestBody Map<String, Object> fields){
        return surveyQuestionService.updateSurveyQuestionPartial(id, fields);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
