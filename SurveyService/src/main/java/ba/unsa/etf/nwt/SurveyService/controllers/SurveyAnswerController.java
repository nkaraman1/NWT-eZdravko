package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.model.SurveyAnswer;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyAnswerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SurveyAnswerController {
    private final SurveyAnswerRepository surveyAnswerRepository;

    public SurveyAnswerController(SurveyAnswerRepository surveyAnswerRepository){
        this.surveyAnswerRepository = surveyAnswerRepository;
    }

    @GetMapping(value="/surveyanswers")
    public List<SurveyAnswer> getSurveyAnswers() {
        return surveyAnswerRepository.findAll();
    }

}