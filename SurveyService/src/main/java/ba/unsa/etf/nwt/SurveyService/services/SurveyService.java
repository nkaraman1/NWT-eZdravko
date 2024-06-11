package ba.unsa.etf.nwt.SurveyService.services;

import ba.unsa.etf.nwt.SurveyService.DTO.NotificationDTO;
import ba.unsa.etf.nwt.SurveyService.DTO.SurveyDTO;
import ba.unsa.etf.nwt.SurveyService.feign.NotificationInterface;
import ba.unsa.etf.nwt.SurveyService.feign.UserInterface;
import ba.unsa.etf.nwt.SurveyService.model.ErrorMsg;
import ba.unsa.etf.nwt.SurveyService.model.Survey;
import ba.unsa.etf.nwt.SurveyService.model.SurveyQuestion;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyRepository;
//import ba.unsa.etf.nwt.UserManagementService.DTO.UserDTO;
//import ba.unsa.etf.nwt.UserManagementService.model.User;
import com.netflix.discovery.converters.Auto;
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
public class SurveyService {

    private final SurveyRepository surveyRepository;

    private final Validator validator;

    @Autowired
    private NotificationInterface notificationInterface;

    @Autowired
    private UserInterface userInterface;

    @Autowired
    public SurveyService(SurveyRepository surveyRepository, Validator validator) {
        this.surveyRepository = surveyRepository;
        this.validator = validator;
    }

    public List<SurveyDTO> getSurveys(){
        List<Survey> surveys = surveyRepository.findAll();
        if (surveys.isEmpty()) {
            return Collections.emptyList();
        }
        return surveys.stream().map(this::convertToDTO).toList();
    }


    public ResponseEntity<?> createSurvey(SurveyDTO surveyDTO){
        Errors errors = new BeanPropertyBindingResult(surveyDTO, "surveyDTO");
        validator.validate(surveyDTO, errors);

        if(errors.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        Survey survey = convertToEntity(surveyDTO);
        survey = surveyRepository.save(survey);
        NotificationDTO newNotification = new NotificationDTO("survey", "Nova anketa:" + survey.getNaslov(), survey.getUser_uid());
        notificationInterface.createNotification(newNotification);
        userInterface.getUserByUID(survey.getUser_uid());
        return new ResponseEntity<>(convertToDTO(survey), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getSurveyByID(Long id) {
        ResponseEntity<?> response = getSurveyByIDNotDTO(id);
        if(response.getStatusCode() != HttpStatus.OK) {
            return response;
        }
        Survey survey = (Survey) response.getBody();
        assert survey != null;
        SurveyDTO surveyDTO = convertToDTO(survey);
        return new ResponseEntity<>(surveyDTO, HttpStatus.OK);
    }

    private ResponseEntity<?> getSurveyByIDNotDTO(Long id) {
        Optional<Survey> optionalSurvey = surveyRepository.findById(id);
        if(optionalSurvey.isEmpty()) {
            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronadjena nijedna anketa sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        Survey survey = optionalSurvey.get();
        return new ResponseEntity<>(survey, HttpStatus.OK);
    }

//    public ResponseEntity<?> getSurveyByNaslov(String naslov) {
//        ResponseEntity<?> response = getSurveyByNaslovNotDTO(naslov);
//        if(response.getStatusCode() != HttpStatus.OK) {
//            return response;
//        }
//        Survey survey = (Survey) response.getBody();
//        assert survey != null;
//        SurveyDTO surveyDTO = convertToDTO(survey);
//        return new ResponseEntity<>(surveyDTO, HttpStatus.OK);
//    }
//
//    private ResponseEntity<?> getSurveyByNaslovNotDTO(String naslov) {
//        Optional<Survey> optionalSurvey = surveyRepository.findByNaslov(naslov);
//        if(optionalSurvey.isEmpty()) {
//            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronadjena nijedna anketa sa tim naslovom."), HttpStatus.NOT_FOUND);
//        }
//        Survey survey = optionalSurvey.get();
//        return new ResponseEntity<>(survey, HttpStatus.OK);
//    }
//
//    public ResponseEntity<?> getSurveyByStatus(Integer status) {
//        ResponseEntity<?> response = getSurveyByStatusNotDTO(status);
//        if(response.getStatusCode() != HttpStatus.OK) {
//            return response;
//        }
//
//        Survey survey = (Survey) response.getBody();
//        assert survey != null;
//        SurveyDTO surveyDTO = convertToDTO(survey);
//        return new ResponseEntity<>(surveyDTO, HttpStatus.OK);
//    }
//
//    private ResponseEntity<?> getSurveyByStatusNotDTO(Integer status) {
//        List<Survey> surveyList = surveyRepository.findByStatus(status);
//        if(surveyList.isEmpty()) {
//            return new ResponseEntity<>(List.of(), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(surveyList, HttpStatus.OK);
//    }

    public ResponseEntity<?> deleteSurvey(Long id) {
        ResponseEntity<?> response = getSurveyByID(id);
        if(response.getStatusCode() == HttpStatus.OK){
            surveyRepository.deleteById(id);
        }
        return response;
    }

    public ResponseEntity<?> updateSurvey(Long id, SurveyDTO surveyDTO){
        ResponseEntity<?> response = getSurveyByIDNotDTO(id);
        if(response.getStatusCode() != HttpStatus.OK) {
            return response;
        }

        Survey survey = (Survey) response.getBody();

        Errors errors = new BeanPropertyBindingResult(surveyDTO, "surveyDTO");
        validator.validate(surveyDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        assert survey != null;
        updateFromDTO(survey, surveyDTO);
        survey = surveyRepository.save(survey);

        NotificationDTO newNotification = new NotificationDTO("survey", "Podaci ankete su promijenjeni!", survey.getUser_uid());
        notificationInterface.createNotification(newNotification);
        userInterface.getUserByUID(survey.getUser_uid());
        return new ResponseEntity<>(convertToDTO(survey), HttpStatus.OK);
    }

    public ResponseEntity<?> updateSurveyPartial(Long id, Map<String, Object> fields){
        Optional<Survey> optionalSurvey = surveyRepository.findById(id);
        if (!optionalSurvey.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena nijedna anketa sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        AtomicBoolean error = new AtomicBoolean(false);
        StringBuilder errorMessage = new StringBuilder();
        fields.forEach((key, value)->{
            Field field = ReflectionUtils.findField(Survey.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, optionalSurvey.get(), value);
            }else{
                error.set(true);
                errorMessage.append("Anketa nema polje ").append(key).append(". ");
            }
        });
        if(error.get()){
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }
        Survey survey = surveyRepository.save(optionalSurvey.get());
        NotificationDTO newNotification = new NotificationDTO("survey", "Podaci ankete su promijenjeni!", survey.getUser_uid());
        notificationInterface.createNotification(newNotification);
        userInterface.getUserByUID(survey.getUser_uid());

        return new ResponseEntity<>(convertToDTO(survey), HttpStatus.OK);
    }

    private Survey updateFromDTO(Survey survey, SurveyDTO surveyDTO) {
        survey.setStatus(surveyDTO.getStatus());
        survey.setNaslov(surveyDTO.getNaslov());
        survey.setOpis(surveyDTO.getOpis());
        survey.setUser_uid(surveyDTO.getUserUid());

        return survey;
    }

    private Survey convertToEntity(SurveyDTO surveyDTO){
        Survey survey = new Survey();
        return updateFromDTO(survey, surveyDTO);
    }

    private SurveyDTO convertToDTO(Survey survey){
        SurveyDTO surveyDTO = new SurveyDTO(
                survey.getUser_uid(),
                survey.getNaslov(),
                survey.getOpis(),
                survey.getStatus()
        );
        return surveyDTO;
    }
}