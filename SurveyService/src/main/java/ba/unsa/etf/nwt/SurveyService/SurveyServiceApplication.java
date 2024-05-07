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
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {"ba.unsa.etf.nwt.SurveyService"})
public class SurveyServiceApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SurveyServiceApplication.class, args);
	}
}