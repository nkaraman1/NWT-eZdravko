package ba.unsa.etf.nwt.NewsService.repositories;

import ba.unsa.etf.nwt.NewsService.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationsRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUid(String uid);
}