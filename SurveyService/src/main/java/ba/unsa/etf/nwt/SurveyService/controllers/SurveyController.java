package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.DTO.SurveyDTO;
import ba.unsa.etf.nwt.SurveyService.model.ErrorMsg;
import ba.unsa.etf.nwt.SurveyService.model.Survey;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyRepository;
import ba.unsa.etf.nwt.SurveyService.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.ReflectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
public class SurveyController {

    private final SurveyService surveyService;

    @Autowired
    public SurveyController(SurveyService surveyService){
        this.surveyService = surveyService;
    }

    @GetMapping(value="/surveys")
    public @ResponseBody List<SurveyDTO> getSurveys(){
        return surveyService.getSurveys();
    }

    @PostMapping(value="/surveys")
    public ResponseEntity<?> createSurvey(@RequestBody SurveyDTO surveyDTO){
        return surveyService.createSurvey(surveyDTO);
    }

    @GetMapping(value = "/surveys/id/{id}")
    public ResponseEntity<?> getSurveyByID(@PathVariable Long id) {
        return surveyService.getSurveyByID(id);
    }

//    @GetMapping(value = "/surveys/naslov/{naslov}")
//    public ResponseEntity<?> getSurveyByNaslov(@PathVariable String naslov) {
//        return surveyService.getSurveyByNaslov(naslov);
//    }
//
//    @GetMapping(value = "/surveys/status/{status}")
//    public ResponseEntity<?> getSurveyByStatus(@PathVariable Integer status) {
//        return surveyService.getSurveyByStatus(status);
//    }

    @DeleteMapping(value = "/surveys/{id}")
    public ResponseEntity<?> deleteSurvey(@PathVariable Long id) {
        return surveyService.deleteSurvey(id);
    }

    @PutMapping(value="/surveys/{id}")
    public ResponseEntity<?> updateSurvey(@PathVariable Long id, @RequestBody SurveyDTO surveyDTO){
        return surveyService.updateSurvey(id, surveyDTO);
    }

    @PatchMapping(value = "/surveys/{id}")
    public ResponseEntity<?> updateSurveyPartial(@PathVariable("id") Long id, @RequestBody Map<String, Object> fields) {
        return surveyService.updateSurveyPartial(id, fields);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}