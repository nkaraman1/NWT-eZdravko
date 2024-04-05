package ba.unsa.etf.nwt.ForumService.controllers;

import ba.unsa.etf.nwt.ForumService.DTO.CommentDTO;
import ba.unsa.etf.nwt.ForumService.DTO.QuestionDTO;
import ba.unsa.etf.nwt.ForumService.exceptions.InvalidQuestionIdException;
import ba.unsa.etf.nwt.ForumService.model.Comment;
import ba.unsa.etf.nwt.ForumService.model.Question;
import ba.unsa.etf.nwt.ForumService.repositories.CommentRepository;
import ba.unsa.etf.nwt.ForumService.repositories.QuestionRepository;
import ba.unsa.etf.nwt.ForumService.exceptions.ErrorMsg;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class CommentController {

    private final CommentRepository commentRepository;
    private final QuestionRepository questionRepository;
    private final Validator validator;


    public CommentController(CommentRepository commentRepository, QuestionRepository questionRepository, Validator validator) {
        this.commentRepository = commentRepository;
        this.questionRepository = questionRepository;
        this.validator = validator;
    }

    @GetMapping(value="/comments")
    public List<CommentDTO> getComments() {
        List<Comment> comments = commentRepository.findAll();
        if (comments.isEmpty()) {
            return Collections.emptyList();
        }
        List<CommentDTO> commentsDTO = new ArrayList<>();
        for (Comment c : comments) {
            commentsDTO.add(new CommentDTO(c.getID(), c.getPitanje().getID(), c.getUser_uid(), c.getSadrzaj(), c.getAnonimnost()));
        }
        return commentsDTO;
    }

    @PostMapping(value="/comments")
    public ResponseEntity<?> createComment(@RequestBody CommentDTO commentDTO) {
        Errors errors = new BeanPropertyBindingResult(commentDTO, "commentDTO");
        validator.validate(commentDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
//            throw new RuntimeException(errorMessage.toString());
        }

        Question question = questionRepository.findById(commentDTO.getQuestionId()).orElse(null);
        if (question == null) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjeno nijedno pitanje sa tim ID-em."), HttpStatus.NOT_FOUND);
//            throw new InvalidQuestionIdException("Nepostojeći question ID: " + commentDTO.getQuestionId());
        }

        Comment comment = new Comment();
        comment.setPitanje(question);
        comment.setUser_uid(commentDTO.getUserUid());
        comment.setSadrzaj(commentDTO.getSadrzaj());
        comment.setAnonimnost(commentDTO.getAnonimnost());

        Comment savedComment = commentRepository.save(comment);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @GetMapping(value="/comments/{commentId}")
    public ResponseEntity<?> getCommentById(@PathVariable("commentId") Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (!optionalComment.isPresent()) {
            return new ResponseEntity<>(new ba.unsa.etf.nwt.ForumService.model.ErrorMsg("not_found", "Nije pronadjeno nijedno pitanje sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        Comment comment = optionalComment.get();
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping(value="/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (!optionalComment.isPresent()) {
            return new ResponseEntity<>(new ba.unsa.etf.nwt.ForumService.model.ErrorMsg("Nije pronadjen nijedan komentar sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        Comment comment = optionalComment.get();

        commentRepository.deleteById(commentId);

        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PutMapping(value="/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentDTO commentDTO) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (!optionalComment.isPresent()) {
            return new ResponseEntity<>(new ba.unsa.etf.nwt.ForumService.model.ErrorMsg("Nije pronadjen nijedan komentar sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        Errors errors = new BeanPropertyBindingResult(commentDTO, "commentDTO");
        validator.validate(commentDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ba.unsa.etf.nwt.ForumService.model.ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
//            throw new RuntimeException(errorMessage.toString());
        }

        Question question = questionRepository.findById(commentDTO.getQuestionId()).orElse(null);
        if (question == null) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjeno nijedno pitanje sa tim ID-em."), HttpStatus.NOT_FOUND);
//            throw new InvalidQuestionIdException("Nepostojeći question ID: " + commentDTO.getQuestionId());
        }

        Comment existingComment = optionalComment.get();

        existingComment.setPitanje(question);
        existingComment.setUser_uid(commentDTO.getUserUid());
        existingComment.setSadrzaj(commentDTO.getSadrzaj());
        existingComment.setAnonimnost(commentDTO.getAnonimnost());

        Comment updatedComment = commentRepository.save(existingComment);

        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @ExceptionHandler(InvalidQuestionIdException.class)
    public ResponseEntity<String> handleInvalidQuestionIdException(InvalidQuestionIdException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
