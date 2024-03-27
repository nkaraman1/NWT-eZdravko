package ba.unsa.etf.nwt.SurveyService.repositories;

import ba.unsa.etf.nwt.SurveyService.model.AnswerOptions;
import ba.unsa.etf.nwt.SurveyService.model.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface AnswerOptionsRepository extends JpaRepository<AnswerOptions, Long> {
    List<AnswerOptions> findByAnketaPitanje(SurveyQuestion surveyQuestion);
}
