package ba.unsa.etf.nwt.ForumService.services;

import ba.unsa.etf.nwt.ForumService.DTO.CommentDTO;
import ba.unsa.etf.nwt.ForumService.exceptions.ErrorMsg;
import ba.unsa.etf.nwt.ForumService.feign.NotificationInterface;
import ba.unsa.etf.nwt.ForumService.feign.UserInterface;
import ba.unsa.etf.nwt.ForumService.model.Comment;
import ba.unsa.etf.nwt.ForumService.model.Question;
import ba.unsa.etf.nwt.ForumService.repositories.CommentRepository;
import ba.unsa.etf.nwt.ForumService.repositories.QuestionRepository;
import ba.unsa.etf.nwt.UserManagementService.model.User;
import ba.unsa.etf.nwt.NewsService.DTO.NotificationDTO;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final QuestionRepository questionRepository;
    private final Validator validator;

    @Autowired
    private UserInterface userInterface;
    @Autowired
    private NotificationInterface notificationInterface;

    @Autowired
    public CommentService(CommentRepository commentRepository, QuestionRepository questionRepository, Validator validator) {
        this.commentRepository = commentRepository;
        this.questionRepository = questionRepository;
        this.validator = validator;
    }

    public List<CommentDTO> getComments() {
        List<Comment> comments = commentRepository.findAll();
        if (comments.isEmpty()) {
            return Collections.emptyList();
        }
        return comments.stream().map(this::convertToDTO).toList();
    }

    public ResponseEntity<?> createComment(CommentDTO commentDTO) {
        Errors errors = new BeanPropertyBindingResult(commentDTO, "commentDTO");
        validator.validate(commentDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        Comment comment = convertToEntity(commentDTO);
        comment = commentRepository.save(comment);

        NotificationDTO newNotification = new NotificationDTO("alert", "Uspješno dodan komentar!", comment.getUser_uid());
        notificationInterface.createNotification(newNotification);
        userInterface.getUserByUID(comment.getUser_uid());
        return new ResponseEntity<>(convertToDTO(comment), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getCommentById(Long commentId) {
        ResponseEntity<?> response = getCommentByIdNotDTO(commentId);
        if (response.getStatusCode() != HttpStatus.OK) {
            return response;
        }
        Comment comment = (Comment) response.getBody();
        assert comment != null;
        CommentDTO commentDTO = convertToDTO(comment);
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    private ResponseEntity<?> getCommentByIdNotDTO(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if(optionalComment.isEmpty()) {
            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronadjen nijedan komentar sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        Comment comment = optionalComment.get();
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteComment(Long commentId) {
        ResponseEntity<?> response = getCommentById(commentId);
        if(response.getStatusCode() == HttpStatus.OK){
            commentRepository.deleteById(commentId);
        }
        return response;
    }

    public ResponseEntity<?> updateComment(Long commentId, CommentDTO commentDTO) {
        ResponseEntity<?> response = getCommentByIdNotDTO(commentId);
        if(response.getStatusCode() != HttpStatus.OK){
            return response;
        }

        Comment comment = (Comment) response.getBody();

        Errors errors = new BeanPropertyBindingResult(commentDTO, "commentDTO");
        validator.validate(commentDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
//            throw new RuntimeException(errorMessage.toString());
        }

        assert comment != null;
        updateFromDTO(comment, commentDTO);
        comment = commentRepository.save(comment);

        return new ResponseEntity<>(convertToDTO(comment), HttpStatus.OK);
    }

    public ResponseEntity<?> updateCommentPartial(Long commentId, Map<String, Object> fields){
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (!optionalComment.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen nijedan komentar sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        AtomicBoolean error = new AtomicBoolean(false);
        StringBuilder errorMessage = new StringBuilder();
        fields.forEach((key, value)->{
            Field field = ReflectionUtils.findField(Comment.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, optionalComment.get(), value);
            }else{
                error.set(true);
                errorMessage.append("Komentar nema polje ").append(key).append(". ");
            }
        });
        if(error.get()){
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }
        Comment comment = commentRepository.save(optionalComment.get());

        return new ResponseEntity<>(convertToDTO(comment), HttpStatus.OK);
    }

    private Comment updateFromDTO(Comment comment, CommentDTO commentDTO) {
        Question question = questionRepository.findById((commentDTO.getQuestionId())).orElse(null);
        comment.setPitanje(question);
        comment.setAnonimnost(commentDTO.getAnonimnost());
        comment.setSadrzaj(commentDTO.getSadrzaj());
        comment.setUser_uid(commentDTO.getUserUid());

        return comment;
    }

    private Comment convertToEntity(CommentDTO commentDTO){
        Comment comment = new Comment();
        return updateFromDTO(comment, commentDTO);
    }

    private CommentDTO convertToDTO(Comment comment){
        CommentDTO commentDTO = new CommentDTO(
                comment.getID(),
                comment.getPitanje().getID(),
                comment.getUser_uid(),
                comment.getSadrzaj(),
                comment.getAnonimnost()
        );

        return commentDTO;
    }

}
