package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.model.SurveyQuestion;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyQuestionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SurveyQuestionController {
    private final SurveyQuestionRepository surveyQuestionRepository;

    public SurveyQuestionController(SurveyQuestionRepository surveyQuestionRepository){
        this.surveyQuestionRepository = surveyQuestionRepository;
    }

    @GetMapping(value="/surveyquestions")
    public List<SurveyQuestion> getSurveyQuestions() {
        return surveyQuestionRepository.findAll();
    }

}
