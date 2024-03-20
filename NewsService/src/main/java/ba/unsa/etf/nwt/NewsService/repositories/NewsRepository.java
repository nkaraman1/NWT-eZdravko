package ba.unsa.etf.nwt.NewsService.repositories;

import ba.unsa.etf.nwt.NewsService.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Integer> {

}
