package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.DTO.SurveyDTO;
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
import java.util.Optional;

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

        Survey survey = surveyRepository.findById((surveyQuestionDTO.getSurveyId())).orElse(null);
        if(survey == null) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena nijedna anketa sa tim ID-em."), HttpStatus.FORBIDDEN);
        }

        SurveyQuestion surveyQuestion = new SurveyQuestion();
        surveyQuestion.setSadrzaj(surveyQuestionDTO.getSadrzaj());
        surveyQuestion.setAnketa(survey);

        SurveyQuestion savedSurveyQuestion = surveyQuestionRepository.save(surveyQuestion);
        return new ResponseEntity<>(savedSurveyQuestion, HttpStatus.CREATED);
    }

    @GetMapping(value = "/surveyquestions/surveyquestion/id/{id}")
    public ResponseEntity<?> getSurveyQuestionByID(@PathVariable Long id) {
        Optional<SurveyQuestion> surveyQuestion = surveyQuestionRepository.findById(id);
        if (surveyQuestion.isPresent()) {
            return new ResponseEntity<>(surveyQuestion.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjeno pitanje na anketi sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/surveyquestions/survey-id/{surveyId}")
    public ResponseEntity<?> getSurveyQuestionBySurveyID(@PathVariable Long surveyId) {
        Optional<Survey> optionalSurvey = surveyRepository.findById(surveyId);
        Survey survey;
        if (optionalSurvey.isPresent()) {
            survey = optionalSurvey.get();
        } else {
            return ResponseEntity.notFound().build();
        }
        List<SurveyQuestion> surveyQuestions = surveyQuestionRepository.findByAnketa(survey);
        if (surveyQuestions.isEmpty()) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(surveyQuestions, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/surveyquestions/surveyquestion/{id}")
    public ResponseEntity<?> deleteSurveyQuestion(@PathVariable Long id) {
        Optional<SurveyQuestion> surveyQuestion = surveyQuestionRepository.findById(id);
        if (surveyQuestion.isPresent()) {
            surveyQuestionRepository.deleteById(id);
//            System.out.println("Tu " + surveyQuestion.get());
//            System.out.println("Tu " + surveyQuestionRepository.findById(id));
            return new ResponseEntity<>(surveyQuestion.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjeno pitanje na anketi sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value="/surveyquestions/surveyquestion/{id}")
    public ResponseEntity<?> updateSurveyQuestion(@PathVariable Long id, @RequestBody SurveyQuestionDTO surveyQuestionDTO){
        Optional<SurveyQuestion> optionalSurveyQuestion = surveyQuestionRepository.findById(id);
        if (!optionalSurveyQuestion.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena nijedno pitanje sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        Errors errors = new BeanPropertyBindingResult(surveyQuestionDTO, "surveyQuestionDTO");
        validator.validate(surveyQuestionDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
//            throw new RuntimeException(errorMessage.toString());
        }

        SurveyQuestion existingSurveyQuestion = optionalSurveyQuestion.get();

        Survey survey = surveyRepository.findById((surveyQuestionDTO.getSurveyId())).orElse(null);

        existingSurveyQuestion.setSadrzaj(surveyQuestionDTO.getSadrzaj());
        existingSurveyQuestion.setAnketa(survey);

        SurveyQuestion updatedSurveyQuestion = surveyQuestionRepository.save(existingSurveyQuestion);

        return new ResponseEntity<>(updatedSurveyQuestion, HttpStatus.OK);
    }

    @PutMapping(value="/surveyquestions/{id}/sadrzaj/{sadrzaj}")
    public ResponseEntity<?> updateSadrzaj(@PathVariable Long id, @PathVariable String sadrzaj) {
        Optional<SurveyQuestion> optionalSurveyQuestion = surveyQuestionRepository.findById(id);
        if (optionalSurveyQuestion.isPresent()) {
            SurveyQuestion surveyQuestion = optionalSurveyQuestion.get();
            surveyQuestion.setSadrzaj(sadrzaj);
            surveyQuestionRepository.save(surveyQuestion);
            return ResponseEntity.ok(surveyQuestion);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Neispravni parametri!"), HttpStatus.NOT_FOUND);        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
