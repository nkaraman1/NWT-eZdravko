package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.DTO.AnswerOptionsDTO;
import ba.unsa.etf.nwt.SurveyService.DTO.SurveyQuestionDTO;
import ba.unsa.etf.nwt.SurveyService.model.AnswerOptions;
import ba.unsa.etf.nwt.SurveyService.model.ErrorMsg;
import ba.unsa.etf.nwt.SurveyService.model.Survey;
import ba.unsa.etf.nwt.SurveyService.model.SurveyQuestion;
import ba.unsa.etf.nwt.SurveyService.repositories.AnswerOptionsRepository;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyQuestionRepository;
import ba.unsa.etf.nwt.SurveyService.services.AnswerOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
public class AnswerOptionsController {

    private final AnswerOptionsService answerOptionsService;

    @Autowired
    public AnswerOptionsController(AnswerOptionsService answerOptionsService) {
        this.answerOptionsService = answerOptionsService;
    }

    @GetMapping(value="/answeroptions")
    public List<AnswerOptionsDTO> getAnswerOptions() {
        return answerOptionsService.getAnswerOptions();
    }

    @PostMapping(value="/answeroptions")
    public ResponseEntity<?> createAnswerOptions(@RequestBody AnswerOptionsDTO answerOptionsDTO){
        return answerOptionsService.createAnswerOptions(answerOptionsDTO);
    }

    @GetMapping(value = "/answeroptions/id/{id}")
    public ResponseEntity<?> getAnswerOptionsByID(@PathVariable Long id) {
        return answerOptionsService.getAnswerOptionsByID(id);
    }

//    @GetMapping(value = "/answeroptions/question-id/{questionId}")
//    public ResponseEntity<?> getAnswerOptionsByQuestionID(@PathVariable Long questionId) {
//        return answerOptionsService.getAnswerOptionsByQuestionID(questionId);
//    }

    @DeleteMapping(value = "/answeroptions/{id}")
    public ResponseEntity<?> deleteAnswerOptions(@PathVariable Long id) {
        return answerOptionsService.deleteAnswerOptions(id);
    }

    @PutMapping(value="/answeroptions/{id}")
    public ResponseEntity<?> updateAnswerOptions(@PathVariable Long id, @RequestBody AnswerOptionsDTO answerOptionsDTO){
        return answerOptionsService.updateAnswerOptions(id, answerOptionsDTO);
    }

    @PatchMapping(value = "/answeroptions/{id}")
    public ResponseEntity<?> updateAnswerOptionsPartial(@PathVariable("id") Long id, @RequestBody Map<String, Object> fields){
        return answerOptionsService.updateAnswerOptionsPartial(id, fields);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
