package ba.unsa.etf.nwt.SurveyService.repositories;

import ba.unsa.etf.nwt.SurveyService.model.Survey;
import ba.unsa.etf.nwt.SurveyService.model.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {

    List<SurveyQuestion> findByAnketa(Survey survey);
}
