package ba.unsa.etf.nwt.ForumService.controllers;

import ba.unsa.etf.nwt.ForumService.DTO.CommentDTO;
import ba.unsa.etf.nwt.ForumService.model.Comment;
import ba.unsa.etf.nwt.ForumService.model.Question;
import ba.unsa.etf.nwt.ForumService.repositories.CommentRepository;
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
public class CommentController {

    private final CommentRepository commentRepository;
    private final QuestionRepository questionRepository;


    public CommentController(CommentRepository commentRepository, QuestionRepository questionRepository) {
        this.commentRepository = commentRepository;
        this.questionRepository = questionRepository;
    }

    @GetMapping(value="/comments")
    public List<Comment> getComments() {
        List<Comment> comments = commentRepository.findAll();
        if (comments.isEmpty()) {
            return Collections.emptyList();
        }
        return comments;
    }

    @PostMapping(value="/comments")
    public ResponseEntity<Comment> createComment(@RequestBody CommentDTO commentDTO) {
        Question question = questionRepository.findById(Math.toIntExact(commentDTO.getQuestionId())).orElse(null);
        if (question == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Or handle the error appropriately
        }

        // Create a new Comment object with the retrieved Question object
        Comment comment = new Comment();
        comment.setPitanje(question);
        comment.setUser_uid(commentDTO.getUserUid());
        comment.setSadrzaj(commentDTO.getSadrzaj());
        comment.setAnonimnost(commentDTO.getAnonimnost());

        // Save the new Comment object
        Comment savedComment = commentRepository.save(comment);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }
}
