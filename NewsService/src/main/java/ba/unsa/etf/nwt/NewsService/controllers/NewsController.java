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
import java.util.Optional;

@RestController
public class NewsController {

    private final NewsRepository newsRepository;
    private final Validator validator;

    public NewsController(NewsRepository newsRepository, Validator validator) {
        this.newsRepository = newsRepository;
        this.validator = validator;
    }

    @GetMapping(value="/news")
    public List<News> getNews() {
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

    @GetMapping(value = "/news/{id}")
    public ResponseEntity<?> getNewsByID(@PathVariable Long id) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isPresent()) {
            return new ResponseEntity<>(news.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena novost sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/news/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isPresent()) {
            newsRepository.deleteById(id);
            return new ResponseEntity<>(news.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena novost sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value="/news/{id}")
    public ResponseEntity<?> updateNews(@PathVariable Long id, @RequestBody NewsDTO newsDTO){
        Optional<News> optionalNews = newsRepository.findById(id);
        if (!optionalNews.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena nijedna novost sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        Errors errors = new BeanPropertyBindingResult(newsDTO, "newsDTO");
        validator.validate(newsDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
//            throw new RuntimeException(errorMessage.toString());
        }

        News existingNews = optionalNews.get();

        existingNews.setUser_uid(newsDTO.getUser_uid());
        existingNews.setNaslov(newsDTO.getNaslov());
        existingNews.setSadrzaj(newsDTO.getSadrzaj());
        existingNews.setSlika(newsDTO.getSlika());

        News updatedNews = newsRepository.save(existingNews);

        return new ResponseEntity<>(updatedNews, HttpStatus.OK);
    }

    @PatchMapping(value="/news/{id}")
    public ResponseEntity<?> patchNews(@PathVariable Long id, @RequestBody NewsDTO newsDTO) {
        Optional<News> optionalNews = newsRepository.findById(id);
        if (!optionalNews.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena nijedna novost sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

//        Errors errors = new BeanPropertyBindingResult(newsDTO, "newsDTO");
//        validator.validate(newsDTO, errors);
//
//        if (errors.hasErrors()) {
//            StringBuilder errorMessage = new StringBuilder();
//            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
//            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
////            throw new RuntimeException(errorMessage.toString());
//        }

        News existingNews = optionalNews.get();

        // Update only the fields that are present in the request
        if (newsDTO.getNaslov() != null) {
            existingNews.setNaslov(newsDTO.getNaslov());
        }
        if (newsDTO.getSadrzaj() != null) {
            existingNews.setSadrzaj(newsDTO.getSadrzaj());
        }
        if (newsDTO.getSlika() != null) {
            existingNews.setSlika(newsDTO.getSlika());
        }
        if (newsDTO.getUser_uid() != null) {
            existingNews.setUser_uid(newsDTO.getUser_uid());
        }

        // Save the updated news
        News updatedNews = newsRepository.save(existingNews);

        return new ResponseEntity<>(updatedNews, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
