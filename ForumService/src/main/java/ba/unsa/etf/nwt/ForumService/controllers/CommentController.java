package ba.unsa.etf.nwt.ForumService.controllers;

import ba.unsa.etf.nwt.ForumService.DTO.CommentDTO;
import ba.unsa.etf.nwt.ForumService.DTO.QuestionDTO;
import ba.unsa.etf.nwt.ForumService.exceptions.InvalidQuestionIdException;
import ba.unsa.etf.nwt.ForumService.model.Comment;
import ba.unsa.etf.nwt.ForumService.model.Question;
import ba.unsa.etf.nwt.ForumService.repositories.CommentRepository;
import ba.unsa.etf.nwt.ForumService.repositories.QuestionRepository;
import ba.unsa.etf.nwt.ForumService.exceptions.ErrorMsg;
import ba.unsa.etf.nwt.ForumService.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @GetMapping(value="/comments")
    public List<CommentDTO> getComments() {
        return commentService.getComments();
    }

    @PostMapping(value="/comments")
    public ResponseEntity<?> createComment(@RequestBody CommentDTO commentDTO) {
        return commentService.createComment(commentDTO);
    }

    @GetMapping(value="/comments/{commentId}")
    public ResponseEntity<?> getCommentById(@PathVariable("commentId") Long commentId) {
        return commentService.getCommentById(commentId);
    }

    @DeleteMapping(value="/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId) {
        return commentService.deleteComment(commentId);
    }

    @PutMapping(value="/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentDTO commentDTO) {
        return commentService.updateComment(commentId, commentDTO);
    }

    @PatchMapping(value="/comments/{commentId}")
    public ResponseEntity<?> updateCommentPartial(@PathVariable("commentId") Long commentId, @RequestBody Map<String, Object> fields) {
        return commentService.updateCommentPartial(commentId, fields);
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
