package ba.unsa.etf.nwt.ForumService.controllers;

import ba.unsa.etf.nwt.ForumService.model.Comment;
import ba.unsa.etf.nwt.ForumService.model.Question;
import ba.unsa.etf.nwt.ForumService.repositories.QuestionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class QuestionController {

    private final QuestionRepository questionRepository;

    public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @GetMapping(value="/questions")
    public List<Question> getQuestions() {
        List<Question> questions = questionRepository.findAll();
        if (questions.isEmpty()) {
            return Collections.emptyList();
        }
        return questions;
    }

    @PostMapping(value="/questions")
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        Question savedQuestion = questionRepository.save(question);
        return new ResponseEntity<>(savedQuestion, HttpStatus.CREATED);
    }
}
