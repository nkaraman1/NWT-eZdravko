package ba.unsa.etf.nwt.ForumService.controllers;

import ba.unsa.etf.nwt.ForumService.DTO.QuestionDTO;
import ba.unsa.etf.nwt.ForumService.model.Comment;
import ba.unsa.etf.nwt.ForumService.model.Question;
import ba.unsa.etf.nwt.ForumService.repositories.QuestionRepository;
import ba.unsa.etf.nwt.UserManagementService.model.ErrorMsg;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class QuestionController {

    private final QuestionRepository questionRepository;
    private final Validator validator;

    public QuestionController(QuestionRepository questionRepository, Validator validator) {
        this.questionRepository = questionRepository;
        this.validator = validator;
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
    public ResponseEntity<?> createQuestion(@RequestBody QuestionDTO questionDTO) {
        Errors errors = new BeanPropertyBindingResult(questionDTO, "questionDTO");
        validator.validate(questionDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
