package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.DTO.AnswerOptionsDTO;
import ba.unsa.etf.nwt.SurveyService.DTO.SurveyQuestionDTO;
import ba.unsa.etf.nwt.SurveyService.model.AnswerOptions;
import ba.unsa.etf.nwt.SurveyService.model.ErrorMsg;
import ba.unsa.etf.nwt.SurveyService.model.Survey;
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
import java.util.Optional;

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

        SurveyQuestion surveyQuestion = surveyQuestionRepository.findById(answerOptionsDTO.getQuestionId()).orElse(null);
        if(surveyQuestion == null) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjeno nijedno pitanje u anketi sa tim ID-em."), HttpStatus.FORBIDDEN);
        }

        AnswerOptions answerOptions = new AnswerOptions();
        answerOptions.setSadrzaj(answerOptionsDTO.getSadrzaj());
        answerOptions.setAnketaPitanje(surveyQuestion);

        AnswerOptions savedAnswerOptions = answerOptionsRepository.save(answerOptions);
        return new ResponseEntity<>(savedAnswerOptions, HttpStatus.CREATED);
    }

    @GetMapping(value = "/answeroptions/answeroption/id/{id}")
    public ResponseEntity<?> getAnswerOptionsByID(@PathVariable Long id) {
        Optional<AnswerOptions> answerOptions = answerOptionsRepository.findById(id);
        if (answerOptions.isPresent()) {
            return new ResponseEntity<>(answerOptions.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen ponudjeni odgovor na pitanju sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/answeroptions/question-id/{questionId}")
    public ResponseEntity<?> getAnswerOptionsByQuestionID(@PathVariable Long questionId) {
        Optional<SurveyQuestion> optionalSurveyQuestion = surveyQuestionRepository.findById(questionId);
        SurveyQuestion surveyQuestion;
        if (optionalSurveyQuestion.isPresent()) {
            surveyQuestion = optionalSurveyQuestion.get();
        } else {
            return ResponseEntity.notFound().build();
        }
        List<AnswerOptions> answerOptions = answerOptionsRepository.findByAnketaPitanje(surveyQuestion);
        if (answerOptions.isEmpty()) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(answerOptions, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/answeroptions/answeroption/{id}")
    public ResponseEntity<?> deleteAnswerOptions(@PathVariable Long id) {
        Optional<AnswerOptions> answerOptions = answerOptionsRepository.findById(id);
        if (answerOptions.isPresent()) {
            answerOptionsRepository.deleteById(id);
            return new ResponseEntity<>(answerOptions.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen ponudjeni odgovor na pitanju sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value="/answeroptions/answeroption/{id}")
    public ResponseEntity<?> updateAnswerOptions(@PathVariable Long id, @RequestBody AnswerOptionsDTO answerOptionsDTO){
        Optional<AnswerOptions> optionalAnswerOptions = answerOptionsRepository.findById(id);
        if (!optionalAnswerOptions.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen nijedan ponudjen odgovor sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        Errors errors = new BeanPropertyBindingResult(answerOptionsDTO, "answerOptionsDTO");
        validator.validate(answerOptionsDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
//            throw new RuntimeException(errorMessage.toString());
        }

        AnswerOptions existingAnswerOptions = optionalAnswerOptions.get();

        SurveyQuestion surveyQuestion = surveyQuestionRepository.findById((answerOptionsDTO.getQuestionId())).orElse(null);

        if(surveyQuestion == null) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjeno nijedno pitanje u anketi sa tim ID-em."), HttpStatus.FORBIDDEN);
        }

        existingAnswerOptions.setSadrzaj(answerOptionsDTO.getSadrzaj());
        existingAnswerOptions.setAnketaPitanje(surveyQuestion);

        AnswerOptions updatedAnswerOptions = answerOptionsRepository.save(existingAnswerOptions);

        return new ResponseEntity<>(updatedAnswerOptions, HttpStatus.OK);
    }

    @PatchMapping(value="/answeroptions/{id}/sadrzaj/{sadrzaj}")
    public ResponseEntity<?> updateSadrzaj(@PathVariable Long id, @PathVariable String sadrzaj) {
        Optional<AnswerOptions> optionalAnswerOptions = answerOptionsRepository.findById(id);
        if (optionalAnswerOptions.isPresent()) {
            AnswerOptions answerOptions = optionalAnswerOptions.get();
            answerOptions.setSadrzaj(sadrzaj);
            answerOptionsRepository.save(answerOptions);
            return ResponseEntity.ok(answerOptions);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Neispravni parametri!"), HttpStatus.NOT_FOUND);        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
