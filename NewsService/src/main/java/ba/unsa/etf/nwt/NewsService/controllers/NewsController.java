package ba.unsa.etf.nwt.NewsService.controllers;

import ba.unsa.etf.nwt.NewsService.model.News;
import ba.unsa.etf.nwt.NewsService.repositories.NewsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class NewsController {

    private final NewsRepository newsRepository;

    public NewsController(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @GetMapping(value="/news")
    public List<News> getComments() {
        List<News> news = newsRepository.findAll();
        if (news.isEmpty()) {
            return Collections.emptyList();
        }
        return news;
    }
}
