package ba.unsa.etf.nwt.NewsService.controllers;

import ba.unsa.etf.nwt.NewsService.model.Notification;
import ba.unsa.etf.nwt.NewsService.repositories.NotificationsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class NotificationController {

    private final NotificationsRepository notificationsRepository;

    public NotificationController(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    @GetMapping(value="/notifications")
    public List<Notification> getQuestions() {
        List<Notification> notifications = notificationsRepository.findAll();
        if (notifications.isEmpty()) {
            return Collections.emptyList();
        }
        return notifications;
    }
}
