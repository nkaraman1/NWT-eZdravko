package ba.unsa.etf.nwt.SurveyService.repositories;

import ba.unsa.etf.nwt.SurveyService.model.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Integer> {

}
