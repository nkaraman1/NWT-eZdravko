package ba.unsa.etf.nwt.SurveyService.repositories;

import ba.unsa.etf.nwt.SurveyService.model.SurveyAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Integer> {
}
