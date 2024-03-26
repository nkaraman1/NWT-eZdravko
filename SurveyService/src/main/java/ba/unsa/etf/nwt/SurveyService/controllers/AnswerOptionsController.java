package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.DTO.AnswerOptionsDTO;
import ba.unsa.etf.nwt.SurveyService.model.AnswerOptions;
import ba.unsa.etf.nwt.SurveyService.model.ErrorMsg;
import ba.unsa.etf.nwt.SurveyService.model.SurveyQuestion;
import ba.unsa.etf.nwt.SurveyService.repositories.AnswerOptionsRepository;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyQuestionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AnswerOptionsController {
    private final AnswerOptionsRepository answerOptionsRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final Validator validator;

    public AnswerOptionsController(AnswerOptionsRepository answerOptionsRepository, SurveyQuestionRepository surveyQuestionRepository, Validator validator){
        this.answerOptionsRepository = answerOptionsRepository;
        this.surveyQuestionRepository = surveyQuestionRepository;
        this.validator = validator;
    }

    @GetMapping(value="/answeroptions")
    public List<AnswerOptions> getAnswerOptions() {
        return answerOptionsRepository.findAll();
    }

    @PostMapping(value="/answeroptions")
    public ResponseEntity<?> createAnswerOptions(@RequestBody AnswerOptionsDTO answerOptionsDTO){
        Errors errors = new BeanPropertyBindingResult(answerOptionsDTO, "answerOptionsDTO");
        validator.validate(answerOptionsDTO, errors);

        if(errors.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        SurveyQuestion surveyQuestion = surveyQuestionRepository.findById(Math.toIntExact(answerOptionsDTO.getQuestionId())).orElse(null);
        if(surveyQuestion == null) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjeno nijedno pitanje u anketi sa tim ID-em."), HttpStatus.FORBIDDEN);
        }

        AnswerOptions answerOptions = new AnswerOptions();
        answerOptions.setSadrzaj(answerOptionsDTO.getSadrzaj());

        AnswerOptions savedAnswerOptions = answerOptionsRepository.save(answerOptions);
        return new ResponseEntity<>(savedAnswerOptions, HttpStatus.CREATED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
