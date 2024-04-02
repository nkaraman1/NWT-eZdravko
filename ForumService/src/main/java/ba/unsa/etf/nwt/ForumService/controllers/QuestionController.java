package ba.unsa.etf.nwt.ForumService.controllers;

import ba.unsa.etf.nwt.ForumService.DTO.QuestionDTO;
import ba.unsa.etf.nwt.ForumService.model.Question;
import ba.unsa.etf.nwt.ForumService.repositories.CommentRepository;
import ba.unsa.etf.nwt.ForumService.repositories.QuestionRepository;
import ba.unsa.etf.nwt.UserManagementService.exceptions.ErrorMsg;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class QuestionController {

    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final Validator validator;

    public QuestionController(QuestionRepository questionRepository, Validator validator, CommentRepository commentRepository) {
        this.questionRepository = questionRepository;
        this.validator = validator;
        this.commentRepository = commentRepository;
    }

    @GetMapping(value="/questions")
    public List<Question> getQuestions() {
        List<Question> questions = questionRepository.findAll();
        if (questions.isEmpty()) {
            return Collections.emptyList();
        }
        return questions;
    }

    @GetMapping(value="/questions/{questionId}")
    public ResponseEntity<?> getQuestionById(@PathVariable("questionId") Long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (!optionalQuestion.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("not_found", "Nije pronadjeno nijedno pitanje sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        Question question = optionalQuestion.get();
        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @PostMapping(value="/questions")
    public ResponseEntity<?> createQuestion(@RequestBody QuestionDTO questionDTO) {
        Errors errors = new BeanPropertyBindingResult(questionDTO, "questionDTO");
        validator.validate(questionDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
//            throw new RuntimeException(errorMessage.toString());
        }
        Question question = new Question();
        question.setUser_uid(questionDTO.getUserUid());
        question.setNaslov(questionDTO.getNaslov());
        question.setSadrzaj(questionDTO.getSadrzaj());
        question.setAnonimnost(question.getAnonimnost());

        Question savedQuestion = questionRepository.save(question);
        return new ResponseEntity<>(savedQuestion, HttpStatus.CREATED);
    }

    @PutMapping(value="/questions/{questionId}")
    public ResponseEntity<?> updateQuestion(@PathVariable("questionId") Long questionId, @RequestBody QuestionDTO questionDTO) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (!optionalQuestion.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjeno nijedno pitanje sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        Errors errors = new BeanPropertyBindingResult(questionDTO, "questionDTO");
        validator.validate(questionDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
//            throw new RuntimeException(errorMessage.toString());
        }

        Question existingQuestion = optionalQuestion.get();

        existingQuestion.setUser_uid(questionDTO.getUserUid());
        existingQuestion.setNaslov(questionDTO.getNaslov());
        existingQuestion.setSadrzaj(questionDTO.getSadrzaj());
        existingQuestion.setAnonimnost(questionDTO.getAnonimnost());

        Question updatedQuestion = questionRepository.save(existingQuestion);

        return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
    }

    @DeleteMapping(value="/questions/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable("questionId") Long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (!optionalQuestion.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjeno nijedno pitanje sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        Question question = optionalQuestion.get();

        List<Comment> comments = question.getComments();

        commentRepository.deleteAll(comments);

        questionRepository.deleteById(questionId);

        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
