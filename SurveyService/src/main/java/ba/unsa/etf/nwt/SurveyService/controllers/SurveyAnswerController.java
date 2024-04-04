package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.DTO.AnswerOptionsDTO;
import ba.unsa.etf.nwt.SurveyService.DTO.SurveyAnswerDTO;
import ba.unsa.etf.nwt.SurveyService.DTO.SurveyQuestionDTO;
import ba.unsa.etf.nwt.SurveyService.model.*;
import ba.unsa.etf.nwt.SurveyService.repositories.AnswerOptionsRepository;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyAnswerRepository;
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
    private final SurveyAnswerRepository surveyAnswerRepository;
    private final AnswerOptionsRepository answerOptionsRepository;

    private final Validator validator;
    public SurveyAnswerController(SurveyAnswerRepository surveyAnswerRepository, AnswerOptionsRepository answerOptionsRepository, Validator validator){
        this.surveyAnswerRepository = surveyAnswerRepository;
        this.answerOptionsRepository = answerOptionsRepository;
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

        AnswerOptions answerOptions = answerOptionsRepository.findById(surveyAnswerDTO.getAnswerId()).orElse(null);
        if(answerOptions == null) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen nijedan ponudjeni odgovor sa tim ID-em."), HttpStatus.FORBIDDEN);
        }

        SurveyAnswer surveyAnswer = new SurveyAnswer();
        surveyAnswer.setAnketaOdgovor(answerOptions);

        SurveyAnswer savedSurveyAnswer = surveyAnswerRepository.save(surveyAnswer);
        return new ResponseEntity<>(savedSurveyAnswer, HttpStatus.CREATED);
    }

    @GetMapping(value = "/surveyanswers/{id}")
    public ResponseEntity<?> getSurveyAnswerByID(@PathVariable Long id) {
        Optional<SurveyAnswer> surveyAnswer = surveyAnswerRepository.findById(id);
        if (surveyAnswer.isPresent()) {
            return new ResponseEntity<>(surveyAnswer.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen odgovor sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/surveyanswers/answer-id/{answerId}")
    public ResponseEntity<?> getSurveyAnswerByAnswerOptionsID(@PathVariable Long answerId) {
        Optional<AnswerOptions> optionalAnswerOptions = answerOptionsRepository.findById(answerId);
        AnswerOptions answerOptions;
        if (optionalAnswerOptions.isPresent()) {
            answerOptions = optionalAnswerOptions.get();
        } else {
            return ResponseEntity.notFound().build();
        }
        List<SurveyAnswer> surveyAnswers = surveyAnswerRepository.findByAnketaOdgovor(answerOptions);
        if (surveyAnswers.isEmpty()) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(surveyAnswers, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/surveyanswers/{id}")
    public ResponseEntity<?> deleteSurveyAnswer(@PathVariable Long id) {
        Optional<SurveyAnswer> surveyAnswer = surveyAnswerRepository.findById(id);
        if (surveyAnswer.isPresent()) {
            surveyAnswerRepository.deleteById(id);
//            System.out.println("Tu " + surveyAnswer.get());
//            System.out.println("Tu " + surveyAnswerRepository.findById(id));
            return new ResponseEntity<>(surveyAnswer.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen odgovor sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value="/surveyanswers/{id}")
    public ResponseEntity<?> updateSurveyAnswer(@PathVariable Long id, @RequestBody SurveyAnswerDTO surveyAnswerDTO){
        Optional<SurveyAnswer> optionalSurveyAnswer = surveyAnswerRepository.findById(id);
        if (!optionalSurveyAnswer.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen nijedan odgovor sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        Errors errors = new BeanPropertyBindingResult(surveyAnswerDTO, "surveyAnswerDTO");
        validator.validate(surveyAnswerDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
//            throw new RuntimeException(errorMessage.toString());
        }

        SurveyAnswer existingSurveyAnswer = optionalSurveyAnswer.get();

        AnswerOptions answerOptions = answerOptionsRepository.findById((surveyAnswerDTO.getAnswerId())).orElse(null);

        if(answerOptions == null) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen nijedan ponudjeni odgovor sa tim ID-em."), HttpStatus.FORBIDDEN);
        }

        existingSurveyAnswer.setAnketaOdgovor(answerOptions);

        SurveyAnswer updatedSurveyAnswer = surveyAnswerRepository.save(existingSurveyAnswer);

        return new ResponseEntity<>(updatedSurveyAnswer, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}