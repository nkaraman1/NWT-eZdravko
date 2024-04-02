package ba.unsa.etf.nwt.ForumService.repositories;

import ba.unsa.etf.nwt.ForumService.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
