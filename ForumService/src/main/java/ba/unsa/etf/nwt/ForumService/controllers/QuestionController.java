package ba.unsa.etf.nwt.ForumService.controllers;

import ba.unsa.etf.nwt.ForumService.DTO.QuestionDTO;
import ba.unsa.etf.nwt.ForumService.model.Comment;
import ba.unsa.etf.nwt.ForumService.model.Question;
import ba.unsa.etf.nwt.ForumService.model.ErrorMsg;
import ba.unsa.etf.nwt.ForumService.repositories.CommentRepository;
import ba.unsa.etf.nwt.ForumService.repositories.QuestionRepository;
import ba.unsa.etf.nwt.ForumService.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping(value="/questions")
    public List<QuestionDTO> getQuestions() {
        return questionService.getQuestions();
    }

    @GetMapping(value="/questions/{questionId}")
    public ResponseEntity<?> getQuestionById(@PathVariable("questionId") Long questionId) {
        return questionService.getQuestionById(questionId);
    }

    @PostMapping(value="/questions")
    public ResponseEntity<?> createQuestion(@RequestBody QuestionDTO questionDTO) {
        return questionService.createQuestion(questionDTO);
    }

    @DeleteMapping(value="/questions/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable("questionId") Long questionId) {
        return questionService.deleteQuestion(questionId);
    }

    @PutMapping(value="/questions/{questionId}")
    public ResponseEntity<?> updateQuestion(@PathVariable("questionId") Long questionId, @RequestBody QuestionDTO questionDTO) {
        return questionService.updateQuestion(questionId, questionDTO);
    }

    @PatchMapping(value="/questions/{questionId}")
    public ResponseEntity<?> updateQuestionPartial(@PathVariable("questionId") Long questionId, @RequestBody Map<String, Object> fields) {
        return questionService.updateQuestionPartial(questionId, fields);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
