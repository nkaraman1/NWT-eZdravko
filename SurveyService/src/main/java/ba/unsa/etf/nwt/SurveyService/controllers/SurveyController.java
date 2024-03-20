package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.model.Survey;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class SurveyController {
    private final SurveyRepository surveyRepository;

    public SurveyController(SurveyRepository surveyRepository){
        this.surveyRepository = surveyRepository;
    }

    @GetMapping(value="/surveys")
    public @ResponseBody List<Survey> getSurveys(){
        List<Survey> surveys = surveyRepository.findAll();
        if (surveys.isEmpty()) {
            // If no surveys found, return an empty list instead of null
            return Collections.emptyList();
        }
        return surveys;
    }

}
