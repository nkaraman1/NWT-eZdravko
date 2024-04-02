package ba.unsa.etf.nwt.ForumService.repositories;

import ba.unsa.etf.nwt.ForumService.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}