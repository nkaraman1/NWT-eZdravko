package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.model.AnswerOptions;
import ba.unsa.etf.nwt.SurveyService.repositories.AnswerOptionsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AnswerOptionsController {
    private final AnswerOptionsRepository answerOptionsRepository;

    public AnswerOptionsController(AnswerOptionsRepository answerOptionsRepository){
        this.answerOptionsRepository = answerOptionsRepository;
    }

    @GetMapping(value="/answeroptions")
    public List<AnswerOptions> getAnswerOptions() {
        return answerOptionsRepository.findAll();
    }
}
