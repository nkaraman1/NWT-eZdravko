package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.DTO.SurveyAnswerDTO;
import ba.unsa.etf.nwt.SurveyService.DTO.SurveyQuestionDTO;
import ba.unsa.etf.nwt.SurveyService.model.ErrorMsg;
import ba.unsa.etf.nwt.SurveyService.model.Survey;
import ba.unsa.etf.nwt.SurveyService.model.SurveyAnswer;
import ba.unsa.etf.nwt.SurveyService.model.SurveyQuestion;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyAnswerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SurveyAnswerController {
    private final SurveyAnswerRepository surveyAnswerRepository;
    private final Validator validator;
    public SurveyAnswerController(SurveyAnswerRepository surveyAnswerRepository, Validator validator){
        this.surveyAnswerRepository = surveyAnswerRepository;
        this.validator = validator;
    }

    @GetMapping(value="/surveyanswers")
    public List<SurveyAnswer> getSurveyAnswers() {
        return surveyAnswerRepository.findAll();
    }

    @PostMapping(value="/surveyanswers")
    public ResponseEntity<?> createSurveyAnswer(@RequestBody SurveyAnswerDTO surveyAnswerDTO){
        Errors errors = new BeanPropertyBindingResult(surveyAnswerDTO, "surveyAnswerDTO");
        validator.validate(surveyAnswerDTO, errors);

        if(errors.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        SurveyAnswer surveyAnswer = surveyAnswerRepository.findById(Math.toIntExact(surveyAnswerDTO.getAnswerId())).orElse(null);
        if(surveyAnswer == null) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen nijedan ponudjeni odgovor sa tim ID-em."), HttpStatus.FORBIDDEN);
        }

        SurveyAnswer savedSurveyAnswer = surveyAnswerRepository.save(surveyAnswer);
        return new ResponseEntity<>(savedSurveyAnswer, HttpStatus.CREATED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}