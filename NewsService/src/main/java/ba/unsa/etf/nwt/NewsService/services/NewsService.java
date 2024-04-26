package ba.unsa.etf.nwt.NewsService.services;

import ba.unsa.etf.nwt.NewsService.DTO.NewsDTO;
import ba.unsa.etf.nwt.NewsService.model.ErrorMsg;
import ba.unsa.etf.nwt.NewsService.model.News;
import ba.unsa.etf.nwt.NewsService.repositories.NewsRepository;
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
public class NewsService {
    private final NewsRepository newsRepository;
    private final Validator validator;

    @Autowired
    public NewsService(NewsRepository newsRepository, Validator validator) {
        this.newsRepository = newsRepository;
        this.validator = validator;
    }

    public List<NewsDTO> getNews() {
        List<News> news = newsRepository.findAll();
        if (news.isEmpty()) {
            return Collections.emptyList();
        }
        return news.stream().map(this::convertToDTO).toList();
    }

    public ResponseEntity<?> createNews(NewsDTO newsDTO) {
        Errors errors = new BeanPropertyBindingResult(newsDTO, "newsDTO");
        validator.validate(newsDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        News news = convertToEntity(newsDTO);
        news = newsRepository.save(news);
        return new ResponseEntity<>(convertToDTO(news), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getNewsByID(Long id) {
        ResponseEntity<?> response = getNewsByIDNotDTO(id);
        if (response.getStatusCode() != HttpStatus.OK) {
            return response;
        }
        News news = (News) response.getBody();
        assert news != null;
        NewsDTO newsDTO = convertToDTO(news);
        return new ResponseEntity<>(newsDTO, HttpStatus.OK);
    }

    private ResponseEntity<?> getNewsByIDNotDTO(Long id) {
        Optional<News> optionalNews = newsRepository.findById(id);
        if(optionalNews.isEmpty()) {
            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronadjena novost sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        News news = optionalNews.get();
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteNews(Long id) {
        ResponseEntity<?> response = getNewsByIDNotDTO(id);
        if(response.getStatusCode() == HttpStatus.OK){
            newsRepository.deleteById(id);
        }
        return response;
    }

    public ResponseEntity<?> updateNews(Long id, NewsDTO newsDTO){
        ResponseEntity<?> response = getNewsByIDNotDTO(id);
        if(response.getStatusCode() != HttpStatus.OK){
            return response;
        }

        News news = (News) response.getBody();

        Errors errors = new BeanPropertyBindingResult(newsDTO, "newsDTO");
        validator.validate(newsDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        assert news != null;
        updateFromDTO(news, newsDTO);
        news = newsRepository.save(news);

        return new ResponseEntity<>(convertToDTO(news), HttpStatus.OK);
    }

    public ResponseEntity<?> updateNewsPartial(Long id, Map<String, Object> fields){
        Optional<News> optionalNews = newsRepository.findById(id);
        if (!optionalNews.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena nijedna novost sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        AtomicBoolean error = new AtomicBoolean(false);
        StringBuilder errorMessage = new StringBuilder();
        fields.forEach((key, value)->{
            Field field = ReflectionUtils.findField(News.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, optionalNews.get(), value);
            }else{
                error.set(true);
                errorMessage.append("Novost nema polje ").append(key).append(". ");
            }
        });
        if(error.get()){
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }
        News news = newsRepository.save(optionalNews.get());

        return new ResponseEntity<>(convertToDTO(news), HttpStatus.OK);
    }

    private News updateFromDTO(News news, NewsDTO newsDTO) {
        news.setSlika(newsDTO.getSlika());
        news.setSadrzaj(newsDTO.getSadrzaj());
        news.setNaslov(newsDTO.getNaslov());
        news.setUser_uid(newsDTO.getUser_uid());
        return news;
    }

    private News convertToEntity(NewsDTO newsDTO){
        News news = new News();
        return updateFromDTO(news, newsDTO);
    }

    private NewsDTO convertToDTO(News news){
        NewsDTO newsDTO = new NewsDTO(
                news.getNaslov(),
                news.getSadrzaj(),
                news.getSlika(),
                news.getUser_uid()
        );

        return newsDTO;
    }
}