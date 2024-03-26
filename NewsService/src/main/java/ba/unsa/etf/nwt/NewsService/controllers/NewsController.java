package ba.unsa.etf.nwt.NewsService.controllers;

import ba.unsa.etf.nwt.NewsService.DTO.NewsDTO;
import ba.unsa.etf.nwt.NewsService.model.ErrorMsg;
import ba.unsa.etf.nwt.NewsService.model.News;
import ba.unsa.etf.nwt.NewsService.repositories.NewsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class NewsController {

    private final NewsRepository newsRepository;
    private final Validator validator;

    public NewsController(NewsRepository newsRepository, Validator validator) {
        this.newsRepository = newsRepository;
        this.validator = validator;
    }

    @GetMapping(value="/news")
    public List<News> getComments() {
        List<News> news = newsRepository.findAll();
        if (news.isEmpty()) {
            return Collections.emptyList();
        }
        return news;
    }

    @PostMapping(value="/news")
    public ResponseEntity<?> createNews(@RequestBody NewsDTO newsDTO) {
        Errors errors = new BeanPropertyBindingResult(newsDTO, "newsDTO");
        validator.validate(newsDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
//            throw new RuntimeException(errorMessage.toString());
        }
        News news = new News();
        news.setNaslov(newsDTO.getNaslov());
        news.setSadrzaj(newsDTO.getSadrzaj());
        news.setSlika(newsDTO.getSlika());
        news.setUser_uid(newsDTO.getUser_uid());

        News savedNews = newsRepository.save(news);
        return new ResponseEntity<>(savedNews, HttpStatus.CREATED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
