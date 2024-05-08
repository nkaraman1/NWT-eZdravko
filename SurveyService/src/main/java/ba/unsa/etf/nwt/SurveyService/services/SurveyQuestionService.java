package ba.unsa.etf.nwt.SurveyService.services;

import ba.unsa.etf.nwt.SurveyService.DTO.SurveyDTO;
import ba.unsa.etf.nwt.SurveyService.DTO.SurveyQuestionDTO;
import ba.unsa.etf.nwt.SurveyService.model.ErrorMsg;
import ba.unsa.etf.nwt.SurveyService.model.Survey;
import ba.unsa.etf.nwt.SurveyService.model.SurveyQuestion;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyQuestionRepository;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyRepository;
import org.apache.tomcat.util.http.parser.HttpParser;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class SurveyQuestionService {

    private final SurveyQuestionRepository surveyQuestionRepository;

    private final SurveyRepository surveyRepository;

    private final Validator validator;

    @Autowired
    public SurveyQuestionService(SurveyQuestionRepository surveyQuestionRepository, SurveyRepository surveyRepository, Validator validator) {
        this.surveyQuestionRepository = surveyQuestionRepository;
        this.surveyRepository = surveyRepository;
        this.validator = validator;
    }

    public List<SurveyQuestionDTO> getSurveyQuestions() {
        List<SurveyQuestion> surveyQuestions = surveyQuestionRepository.findAll();
        if(surveyQuestions.isEmpty()){
            return Collections.emptyList();
        }
        return surveyQuestions.stream().map(this::convertToDTO).toList();
    }

    public ResponseEntity<?> createSurveyQuestion(SurveyQuestionDTO surveyQuestionDTO){
        Errors errors = new BeanPropertyBindingResult(surveyQuestionDTO, "surveyQuestionDTO");
        validator.validate(surveyQuestionDTO, errors);

        if(errors.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        Survey survey = surveyRepository.findById((surveyQuestionDTO.getSurveyId())).orElse(null);
        if(survey == null) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena nijedna anketa sa tim ID-em."), HttpStatus.FORBIDDEN);
        }

        SurveyQuestion surveyQuestion = convertToEntity(surveyQuestionDTO);
        surveyQuestion = surveyQuestionRepository.save(surveyQuestion);

        return new ResponseEntity<>(convertToDTO(surveyQuestion), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getSurveyQuestionByID(Long id) {
        ResponseEntity<?> response = getSurveyQuestionByIDNotDTO(id);
        if(response.getStatusCode() != HttpStatus.OK) {
            return response;
        }
        SurveyQuestion surveyQuestion = (SurveyQuestion) response.getBody();
        assert surveyQuestion != null;
        SurveyQuestionDTO surveyQuestionDTO = convertToDTO(surveyQuestion);
        return new ResponseEntity<>(surveyQuestionDTO, HttpStatus.OK);
    }

    private ResponseEntity<?> getSurveyQuestionByIDNotDTO(Long id) {
        Optional<SurveyQuestion> optionalSurveyQuestion = surveyQuestionRepository.findById(id);
        if(optionalSurveyQuestion.isEmpty()) {
            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronadjeno nijedno pitanje sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        SurveyQuestion surveyQuestion = optionalSurveyQuestion.get();
        return new ResponseEntity<>(surveyQuestion, HttpStatus.OK);
    }


//    public ResponseEntity<?> getSurveyQuestionBySurveyID(Long surveyId) {
//        ResponseEntity<?> response = getSurveyQuestionBySurveyIDNotDTO(surveyId);
//        if(response.getStatusCode() != HttpStatus.OK){
//            return response;
//        }
//
//        SurveyQuestion surveyQuestion = (SurveyQuestion) response.getBody();
//        assert surveyQuestion != null;
//        SurveyQuestionDTO surveyQuestionDTO = convertToDTO(surveyQuestion);
//        return new ResponseEntity<>(surveyQuestionDTO, HttpStatus.OK);
//    }
//
//    private ResponseEntity<?> getSurveyQuestionBySurveyIDNotDTO(Long surveyId) {
//        Optional<Survey> optionalSurvey = surveyRepository.findById(surveyId);
//        Survey survey;
//        if (optionalSurvey.isPresent()) {
//            survey = optionalSurvey.get();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//        List<SurveyQuestion> surveyQuestionList = surveyQuestionRepository.findByAnketa(survey);
//        if(surveyQuestionList.isEmpty()) {
//            return new ResponseEntity<>(List.of(), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(surveyQuestionList, HttpStatus.OK);
//    }

    public ResponseEntity<?> deleteSurveyQuestion(Long id) {
        ResponseEntity<?> response = getSurveyQuestionByID(id);
        if(response.getStatusCode() == HttpStatus.OK){
            surveyQuestionRepository.deleteById(id);
        }
        return response;
    }

    public ResponseEntity<?> updateSurveyQuestion(Long id, SurveyQuestionDTO surveyQuestionDTO){
        ResponseEntity<?> response = getSurveyQuestionByIDNotDTO(id);
        if(response.getStatusCode() != HttpStatus.OK){
            return response;
        }

        SurveyQuestion surveyQuestion = (SurveyQuestion) response.getBody();

        Errors errors = new BeanPropertyBindingResult(surveyQuestionDTO, "surveyQuestionDTO");
        validator.validate(surveyQuestionDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        assert surveyQuestion != null;
        updateFromDTO(surveyQuestion, surveyQuestionDTO);
        surveyQuestion = surveyQuestionRepository.save(surveyQuestion);

        return new ResponseEntity<>(convertToDTO(surveyQuestion), HttpStatus.OK);
    }

    public ResponseEntity<?> updateSurveyQuestionPartial(@PathVariable("id") Long id, @RequestBody Map<String, Object> fields){
        Optional<SurveyQuestion> optionalSurveyQuestion = surveyQuestionRepository.findById(id);
        if (!optionalSurveyQuestion.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena nijedno pitanje sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        AtomicBoolean error = new AtomicBoolean(false);
        StringBuilder errorMessage = new StringBuilder();
        fields.forEach((key, value)->{
            Field field = ReflectionUtils.findField(SurveyQuestion.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, optionalSurveyQuestion.get(), value);
            }else{
                error.set(true);
                errorMessage.append("Pitanje nema polje ").append(key).append(". ");
            }
        });
        if(error.get()){
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }
        SurveyQuestion surveyQuestion = surveyQuestionRepository.save(optionalSurveyQuestion.get());

        return new ResponseEntity<>(convertToDTO(surveyQuestion), HttpStatus.OK);
    }

    private SurveyQuestion updateFromDTO(SurveyQuestion surveyQuestion, SurveyQuestionDTO surveyQuestionDTO) {
        Survey survey = surveyRepository.findById((surveyQuestionDTO.getSurveyId())).orElse(null);
        surveyQuestion.setAnketa(survey);
        surveyQuestion.setSadrzaj(surveyQuestionDTO.getSadrzaj());

        return surveyQuestion;
    }

    private SurveyQuestion convertToEntity(SurveyQuestionDTO surveyQuestionDTO){
        SurveyQuestion surveyQuestion = new SurveyQuestion();
        return updateFromDTO(surveyQuestion, surveyQuestionDTO);
    }

    private SurveyQuestionDTO convertToDTO(SurveyQuestion surveyQuestion){
        SurveyQuestionDTO surveyQuestionDTO = new SurveyQuestionDTO(
                surveyQuestion.getAnketa().getID(),
                surveyQuestion.getSadrzaj()
        );

        return surveyQuestionDTO;
    }
}