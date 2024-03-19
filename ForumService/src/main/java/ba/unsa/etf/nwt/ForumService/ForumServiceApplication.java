package ba.unsa.etf.nwt.ForumService;

import ba.unsa.etf.nwt.ForumService.model.Question;
import ba.unsa.etf.nwt.ForumService.model.Comment;
import ba.unsa.etf.nwt.ForumService.repositories.QuestionRepository;
import ba.unsa.etf.nwt.ForumService.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class ForumServiceApplication {
	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private CommentRepository commentRepository;
	public static void main(String[] args) throws Exception {

		SpringApplication.run(ForumServiceApplication.class, args);
	}

	@GetMapping(value="/questions")
	public @ResponseBody List<Question> getQuestions() {
		List<Question> questions = questionRepository.findAll();
		if (questions.isEmpty()) {
			// If no questions found, return an empty list instead of null
			return Collections.emptyList();
		}
		return questions;
	}

	@GetMapping(value="/comments")
	public List<Comment> getComments() {
		return commentRepository.findAll();
	}

}
