package ba.unsa.etf.nwt.SurveyService;

import ba.unsa.etf.nwt.SurveyService.model.AnswerOptions;
import ba.unsa.etf.nwt.SurveyService.model.Survey;
import ba.unsa.etf.nwt.SurveyService.model.SurveyAnswer;
import ba.unsa.etf.nwt.SurveyService.model.SurveyQuestion;
import ba.unsa.etf.nwt.SurveyService.repositories.AnswerOptionsRepository;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyAnswerRepository;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyQuestionRepository;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class SurveyServiceApplication {

	@Autowired
	private SurveyRepository surveyRepository;
	@Autowired
	private SurveyQuestionRepository surveyQuestionRepository;
	@Autowired
	private AnswerOptionsRepository answerOptionsRepository;
	@Autowired
	private SurveyAnswerRepository surveyAnswerRepository;
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SurveyServiceApplication.class, args);
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

	@GetMapping(value="/surveyquestions")
	public List<SurveyQuestion> getSurveyQuestions() {
		return surveyQuestionRepository.findAll();
	}

	@GetMapping(value="/answeroptions")
	public List<AnswerOptions> getAnswerOptions() {
		return answerOptionsRepository.findAll();
	}

	@GetMapping(value="/surveyanswers")
	public List<SurveyAnswer> getSurveyAnswers() {
		return surveyAnswerRepository.findAll();
	}
}
