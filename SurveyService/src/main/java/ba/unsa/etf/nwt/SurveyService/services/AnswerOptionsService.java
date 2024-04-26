package ba.unsa.etf.nwt.SurveyService.services;

import ba.unsa.etf.nwt.SurveyService.DTO.AnswerOptionsDTO;
import ba.unsa.etf.nwt.SurveyService.DTO.SurveyQuestionDTO;
import ba.unsa.etf.nwt.SurveyService.model.AnswerOptions;
import ba.unsa.etf.nwt.SurveyService.model.ErrorMsg;
import ba.unsa.etf.nwt.SurveyService.model.Survey;
import ba.unsa.etf.nwt.SurveyService.model.SurveyQuestion;
import ba.unsa.etf.nwt.SurveyService.repositories.AnswerOptionsRepository;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class AnswerOptionsService {
    private final AnswerOptionsRepository answerOptionsRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final Validator validator;

    @Autowired
    public AnswerOptionsService(AnswerOptionsRepository answerOptionsRepository, SurveyQuestionRepository surveyQuestionRepository, Validator validator){
        this.answerOptionsRepository = answerOptionsRepository;
        this.surveyQuestionRepository = surveyQuestionRepository;
        this.validator = validator;
    }

    public List<AnswerOptionsDTO> getAnswerOptions() {
        List<AnswerOptions> answerOptions = answerOptionsRepository.findAll();
        if(answerOptions.isEmpty()){
            return Collections.emptyList();
        }
        return answerOptions.stream().map(this::convertToDTO).toList();
    }

    public ResponseEntity<?> createAnswerOptions(AnswerOptionsDTO answerOptionsDTO){
        Errors errors = new BeanPropertyBindingResult(answerOptionsDTO, "answerOptionsDTO");
        validator.validate(answerOptionsDTO, errors);

        if(errors.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        SurveyQuestion surveyQuestion = surveyQuestionRepository.findById(answerOptionsDTO.getQuestionId()).orElse(null);
        if(surveyQuestion == null) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjeno nijedno pitanje u anketi sa tim ID-em."), HttpStatus.FORBIDDEN);
        }

        AnswerOptions answerOptions = convertToEntity(answerOptionsDTO);
        answerOptions = answerOptionsRepository.save(answerOptions);

        return new ResponseEntity<>(convertToDTO(answerOptions), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getAnswerOptionsByID(Long id) {
        ResponseEntity<?> response = getAnswerOptionsByIDNotDTO(id);
        if(response.getStatusCode() != HttpStatus.OK) {
            return response;
        }
        AnswerOptions answerOptions = (AnswerOptions) response.getBody();
        assert answerOptions != null;
        AnswerOptionsDTO answerOptionsDTO = convertToDTO(answerOptions);
        return new ResponseEntity<>(answerOptionsDTO, HttpStatus.OK);
    }

    private ResponseEntity<?> getAnswerOptionsByIDNotDTO(Long id) {
        Optional<AnswerOptions> optionalAnswerOptions = answerOptionsRepository.findById(id);
        if(optionalAnswerOptions.isEmpty()){
            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronadjen nijedan ponudjen odgovor sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        AnswerOptions answerOptions = optionalAnswerOptions.get();
        return new ResponseEntity<>(answerOptions, HttpStatus.OK);
    }

//    public ResponseEntity<?> getAnswerOptionsByQuestionID(Long questionId) {
//        ResponseEntity<?> response = getAnswerOptionsByQuestionIDNotDTO(questionId);
//        if(response.getStatusCode() != HttpStatus.OK) {
//            return response;
//        }
//        AnswerOptions answerOptions = (AnswerOptions) response.getBody();
//        assert answerOptions != null;
//        AnswerOptionsDTO answerOptionsDTO = convertToDTO(answerOptions);
//        return new ResponseEntity<>(answerOptionsDTO, HttpStatus.OK);
//    }
//
//    private ResponseEntity<?> getAnswerOptionsByQuestionIDNotDTO(Long questionId) {
//        Optional<SurveyQuestion> optionalSurveyQuestion = surveyQuestionRepository.findById(questionId);
//        SurveyQuestion surveyQuestion;
//        if(optionalSurveyQuestion.isPresent()){
//            surveyQuestion = optionalSurveyQuestion.get();
//        }
//        else{
//            return ResponseEntity.notFound().build();
//        }
//        List<AnswerOptions> answerOptionsList = answerOptionsRepository.findByAnketaPitanje(surveyQuestion);
//        if(answerOptionsList.isEmpty()){
//            return new ResponseEntity<>(List.of(), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(answerOptionsList, HttpStatus.OK);
//    }

    public ResponseEntity<?> deleteAnswerOptions(Long id) {
        ResponseEntity<?> response = getAnswerOptionsByID(id);
        if(response.getStatusCode() == HttpStatus.OK){
            answerOptionsRepository.deleteById(id);
        }
        return response;
    }

    public ResponseEntity<?> updateAnswerOptions(Long id, AnswerOptionsDTO answerOptionsDTO){
        ResponseEntity<?> response = getAnswerOptionsByIDNotDTO(id);
        if(response.getStatusCode() != HttpStatus.OK){
            return response;
        }

        AnswerOptions answerOptions = (AnswerOptions) response.getBody();

        Errors errors = new BeanPropertyBindingResult(answerOptionsDTO, "answerOptionsDTO");
        validator.validate(answerOptionsDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        assert answerOptions != null;
        updateFromDTO(answerOptions, answerOptionsDTO);
        answerOptions = answerOptionsRepository.save(answerOptions);

        return new ResponseEntity<>(convertToDTO(answerOptions), HttpStatus.OK);
    }

    public ResponseEntity<?> updateAnswerOptionsPartial(@PathVariable("id") Long id, @RequestBody Map<String, Object> fields){
        Optional<AnswerOptions> optionalAnswerOptions = answerOptionsRepository.findById(id);
        if (!optionalAnswerOptions.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen nijedan ponudjen odgovor sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        AtomicBoolean error = new AtomicBoolean(false);
        StringBuilder errorMessage = new StringBuilder();
        fields.forEach((key, value)->{
            Field field = ReflectionUtils.findField(AnswerOptions.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, optionalAnswerOptions.get(), value);
            }else{
                error.set(true);
                errorMessage.append("Ponudjeni odgovor nema polje ").append(key).append(". ");
            }
        });
        if(error.get()){
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }
        AnswerOptions answerOptions = answerOptionsRepository.save(optionalAnswerOptions.get());

        return new ResponseEntity<>(convertToDTO(answerOptions), HttpStatus.OK);
    }

    private AnswerOptions updateFromDTO(AnswerOptions answerOptions, AnswerOptionsDTO answerOptionsDTO) {
        SurveyQuestion surveyQuestion = surveyQuestionRepository.findById((answerOptionsDTO.getQuestionId())).orElse(null);
        answerOptions.setAnketaPitanje(surveyQuestion);
        answerOptions.setSadrzaj(answerOptionsDTO.getSadrzaj());

        return answerOptions;
    }

    private AnswerOptions convertToEntity(AnswerOptionsDTO answerOptionsDTO){
        AnswerOptions answerOptions = new AnswerOptions();
        return updateFromDTO(answerOptions, answerOptionsDTO);
    }

    private AnswerOptionsDTO convertToDTO(AnswerOptions answerOptions){
        AnswerOptionsDTO answerOptionsDTO = new AnswerOptionsDTO(
                answerOptions.getAnketaPitanje().getID(),
                answerOptions.getSadrzaj()
        );

        return answerOptionsDTO;
    }
}
