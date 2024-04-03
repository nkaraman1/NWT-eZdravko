package ba.unsa.etf.nwt.NewsService.repositories;

import ba.unsa.etf.nwt.NewsService.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationsRepository extends JpaRepository<Notification, Long> {

}