package ba.unsa.etf.nwt.SurveyService.services;

import ba.unsa.etf.nwt.SurveyService.DTO.SurveyAnswerDTO;
import ba.unsa.etf.nwt.SurveyService.DTO.SurveyQuestionDTO;
import ba.unsa.etf.nwt.SurveyService.model.*;
import ba.unsa.etf.nwt.SurveyService.repositories.AnswerOptionsRepository;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyAnswerService {
    private final SurveyAnswerRepository surveyAnswerRepository;
    private final AnswerOptionsRepository answerOptionsRepository;

    private final Validator validator;

    @Autowired
    public SurveyAnswerService(SurveyAnswerRepository surveyAnswerRepository, AnswerOptionsRepository answerOptionsRepository, Validator validator){
        this.surveyAnswerRepository = surveyAnswerRepository;
        this.answerOptionsRepository = answerOptionsRepository;
        this.validator = validator;
    }

    public List<SurveyAnswerDTO> getSurveyAnswers() {
        List<SurveyAnswer> surveyAnswers = surveyAnswerRepository.findAll();
        if(surveyAnswers.isEmpty()){
            return Collections.emptyList();
        }
        return surveyAnswers.stream().map(this::convertToDTO).toList();
    }

    public ResponseEntity<?> createSurveyAnswer(SurveyAnswerDTO surveyAnswerDTO){
        Errors errors = new BeanPropertyBindingResult(surveyAnswerDTO, "surveyAnswerDTO");
        validator.validate(surveyAnswerDTO, errors);

        if(errors.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        AnswerOptions answerOptions = answerOptionsRepository.findById(surveyAnswerDTO.getAnswerId()).orElse(null);
        if(answerOptions == null) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen nijedan ponudjeni odgovor sa tim ID-em."), HttpStatus.FORBIDDEN);
        }

        SurveyAnswer surveyAnswer = convertToEntity(surveyAnswerDTO);
        surveyAnswer = surveyAnswerRepository.save(surveyAnswer);

        return new ResponseEntity<>(convertToDTO(surveyAnswer), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getSurveyAnswerByID(Long id) {
        ResponseEntity<?> response = getSurveyAnswerByIDNotDTO(id);
        if(response.getStatusCode() != HttpStatus.OK) {
            return response;
        }
        SurveyAnswer surveyAnswer = (SurveyAnswer) response.getBody();
        assert surveyAnswer != null;
        SurveyAnswerDTO surveyAnswerDTO = convertToDTO(surveyAnswer);
        return new ResponseEntity<>(surveyAnswerDTO, HttpStatus.OK);
    }

    private ResponseEntity<?> getSurveyAnswerByIDNotDTO(Long id) {
        Optional<SurveyAnswer> optionalSurveyAnswer = surveyAnswerRepository.findById(id);
        if(optionalSurveyAnswer.isEmpty()) {
            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronadjen nijedan odgovor sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        SurveyAnswer surveyAnswer = optionalSurveyAnswer.get();
        return new ResponseEntity<>(surveyAnswer, HttpStatus.OK);
    }

//    public ResponseEntity<?> getSurveyAnswerByAnswerOptionsID(Long answerId) {
//        ResponseEntity<?> response = getSurveyAnswerByAnswerOptionsIDNotDTO(answerId);
//        if(response.getStatusCode() != HttpStatus.OK){
//            return response;
//        }
//
//        SurveyAnswer surveyAnswer = (SurveyAnswer) response.getBody();
//        assert surveyAnswer != null;
//        SurveyAnswerDTO surveyAnswerDTO = convertToDTO(surveyAnswer);
//        return new ResponseEntity<>(surveyAnswerDTO, HttpStatus.OK);
//    }
//
//    private ResponseEntity<?> getSurveyAnswerByAnswerOptionsIDNotDTO(Long answerId) {
//        Optional<AnswerOptions> optionalAnswerOptions = answerOptionsRepository.findById(answerId);
//        AnswerOptions answerOptions;
//        if (optionalAnswerOptions.isPresent()) {
//            answerOptions = optionalAnswerOptions.get();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//        List<SurveyAnswer> surveyAnswerList = surveyAnswerRepository.findByAnketaOdgovor(answerOptions);
//        if(surveyAnswerList.isEmpty()) {
//            return new ResponseEntity<>(List.of(), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(surveyAnswerList, HttpStatus.OK);
//    }

    public ResponseEntity<?> deleteSurveyAnswer(Long id) {
        ResponseEntity<?> response = getSurveyAnswerByID(id);
        if(response.getStatusCode() == HttpStatus.OK){
            surveyAnswerRepository.deleteById(id);
        }
        return response;
    }

    public ResponseEntity<?> updateSurveyAnswer(Long id, SurveyAnswerDTO surveyAnswerDTO){
        ResponseEntity<?> response = getSurveyAnswerByIDNotDTO(id);
        if(response.getStatusCode() != HttpStatus.OK){
            return response;
        }

        SurveyAnswer surveyAnswer = (SurveyAnswer) response.getBody();

        Errors errors = new BeanPropertyBindingResult(surveyAnswerDTO, "surveyAnswerDTO");
        validator.validate(surveyAnswerDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        assert surveyAnswer != null;
        updateFromDTO(surveyAnswer, surveyAnswerDTO);
        surveyAnswer = surveyAnswerRepository.save(surveyAnswer);
        return new ResponseEntity<>(convertToDTO(surveyAnswer), HttpStatus.OK);
    }

    private SurveyAnswer updateFromDTO(SurveyAnswer surveyAnswer, SurveyAnswerDTO surveyAnswerDTO) {
        AnswerOptions answerOptions = answerOptionsRepository.findById((surveyAnswerDTO.getAnswerId())).orElse(null);
        surveyAnswer.setAnketaOdgovor(answerOptions);
        return surveyAnswer;
    }

    private SurveyAnswer convertToEntity(SurveyAnswerDTO surveyAnswerDTO){
        SurveyAnswer surveyAnswer = new SurveyAnswer();
        return updateFromDTO(surveyAnswer, surveyAnswerDTO);
    }

    private SurveyAnswerDTO convertToDTO(SurveyAnswer surveyAnswer){
        SurveyAnswerDTO surveyAnswerDTO = new SurveyAnswerDTO(
                surveyAnswer.getAnketaOdgovor().getID()
        );

        return surveyAnswerDTO;
    }
}
