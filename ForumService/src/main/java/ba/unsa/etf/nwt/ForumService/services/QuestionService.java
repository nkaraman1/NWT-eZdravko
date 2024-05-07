package ba.unsa.etf.nwt.ForumService.services;

import ba.unsa.etf.nwt.ForumService.DTO.CommentDTO;
import ba.unsa.etf.nwt.ForumService.DTO.QuestionDTO;
import ba.unsa.etf.nwt.ForumService.feign.UserInterface;
import ba.unsa.etf.nwt.ForumService.model.Comment;
import ba.unsa.etf.nwt.ForumService.model.ErrorMsg;
import ba.unsa.etf.nwt.ForumService.model.Question;
import ba.unsa.etf.nwt.ForumService.repositories.CommentRepository;
import ba.unsa.etf.nwt.ForumService.repositories.QuestionRepository;
import ba.unsa.etf.nwt.UserManagementService.model.User;
import ba.unsa.etf.nwt.UserManagementService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final Validator validator;

    @Autowired
    private UserInterface userInterface;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, Validator validator, CommentRepository commentRepository, UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.validator = validator;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public List<QuestionDTO> getQuestions() {
        List<Question> questions = questionRepository.findAll();
        if (questions.isEmpty()) {
            return Collections.emptyList();
        }
        return questions.stream().map(this::convertToDTO).toList();
    }

    public ResponseEntity<?> getQuestionById(Long questionId) {
        ResponseEntity<?> response = getQuestionByIdNotDTO(questionId);
        if (response.getStatusCode() != HttpStatus.OK) {
            return response;
        }
        Question question = (Question) response.getBody();
        assert question != null;
        QuestionDTO questionDTO = convertToDTO(question);
        return new ResponseEntity<>(questionDTO, HttpStatus.OK);
    }

    private ResponseEntity<?> getQuestionByIdNotDTO(Long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if(optionalQuestion.isEmpty()) {
            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronadjeno nijedno pitanje sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        Question question = optionalQuestion.get();
        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    public ResponseEntity<?> createQuestion(QuestionDTO questionDTO) {
        Errors errors = new BeanPropertyBindingResult(questionDTO, "questionDTO");
        validator.validate(questionDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
//            throw new RuntimeException(errorMessage.toString());
        }

        Question question = convertToEntity(questionDTO);
        question = questionRepository.save(question);

        Optional<User> optionalUser = userRepository.findByUID(question.getUser_uid());
        if(optionalUser.isEmpty()) {
            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronadjen nijedan korisnik sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        userInterface.getUserByID(user.getID());
        return new ResponseEntity<>(convertToDTO(question), HttpStatus.CREATED);
    }


    public ResponseEntity<?> deleteQuestion(Long questionId) {
        ResponseEntity<?> response = getQuestionByIdNotDTO(questionId);
        if(response.getStatusCode() == HttpStatus.OK){
            Question question = (Question) response.getBody();
            List<Comment> comments = question.getComments();
            commentRepository.deleteAll(comments);
            questionRepository.deleteById(questionId);
        }
        return response;
    }

    public ResponseEntity<?> updateQuestion(Long questionId, QuestionDTO questionDTO) {
        ResponseEntity<?> response = getQuestionByIdNotDTO(questionId);
        if(response.getStatusCode() != HttpStatus.OK){
            return response;
        }

        Question question = (Question) response.getBody();

        Errors errors = new BeanPropertyBindingResult(questionDTO, "questionDTO");
        validator.validate(questionDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
//            throw new RuntimeException(errorMessage.toString());
        }

        assert question != null;
        updateFromDTO(question, questionDTO);
        question = questionRepository.save(question);

        return new ResponseEntity<>(convertToDTO(question), HttpStatus.OK);
    }

    public ResponseEntity<?> updateQuestionPartial(Long questionId, Map<String, Object> fields){
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (!optionalQuestion.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjeno nijedno pitanje sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        AtomicBoolean error = new AtomicBoolean(false);
        StringBuilder errorMessage = new StringBuilder();
        fields.forEach((key, value)->{
            Field field = ReflectionUtils.findField(Question.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, optionalQuestion.get(), value);
            }else{
                error.set(true);
                errorMessage.append("Pitanje nema polje ").append(key).append(". ");
            }
        });
        if(error.get()){
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }
        Question question = questionRepository.save(optionalQuestion.get());

        return new ResponseEntity<>(convertToDTO(question), HttpStatus.OK);
    }

    private Question updateFromDTO(Question question, QuestionDTO questionDTO) {
        question.setAnonimnost(questionDTO.getAnonimnost());
        question.setSadrzaj(questionDTO.getSadrzaj());
        question.setNaslov(questionDTO.getNaslov());
        question.setUser_uid(questionDTO.getUserUid());

        return question;
    }

    private Question convertToEntity(QuestionDTO questionDTO){
        Question question = new Question();
        return updateFromDTO(question, questionDTO);
    }

    private QuestionDTO convertToDTO(Question question){
        QuestionDTO questionDTO = new QuestionDTO(
                question.getID(),
                question.getUser_uid(),
                question.getNaslov(),
                question.getSadrzaj(),
                question.getAnonimnost()
        );

        return questionDTO;
    }
}
