package ba.unsa.etf.nwt.NewsService.controllers;

import ba.unsa.etf.nwt.NewsService.DTO.NewsDTO;
import ba.unsa.etf.nwt.NewsService.model.ErrorMsg;
import ba.unsa.etf.nwt.NewsService.model.News;
import ba.unsa.etf.nwt.NewsService.repositories.NewsRepository;
import ba.unsa.etf.nwt.NewsService.services.NewsService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @CrossOrigin    //ovo je da se moze preko fronta pristupit
    @GetMapping(value="/news")
    public List<NewsDTO> getNews() {
        return newsService.getNews();
    }

    @PostMapping(value="/news")
    public ResponseEntity<?> createNews(@RequestBody NewsDTO newsDTO) {
        return newsService.createNews(newsDTO);
    }

    @GetMapping(value = "/news/{id}")
    public ResponseEntity<?> getNewsByID(@PathVariable Long id) {
        return newsService.getNewsByID(id);
    }

    @DeleteMapping(value = "/news/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        return newsService.deleteNews(id);
    }

    @PutMapping(value="/news/{id}")
    public ResponseEntity<?> updateNews(@PathVariable Long id, @RequestBody NewsDTO newsDTO){
        return newsService.updateNews(id, newsDTO);
    }

    @PatchMapping(value = "/news/{id}")
    public ResponseEntity<?> updateNewsPartial(@PathVariable("id") Long id, @RequestBody Map<String, Object> fields){
        return newsService.updateNewsPartial(id, fields);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
