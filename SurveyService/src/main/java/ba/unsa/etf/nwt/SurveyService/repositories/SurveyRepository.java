package ba.unsa.etf.nwt.SurveyService.repositories;

import ba.unsa.etf.nwt.SurveyService.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Integer> {

}
