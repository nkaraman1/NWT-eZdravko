package ba.unsa.etf.nwt.NewsService.controllers;

import ba.unsa.etf.nwt.NewsService.DTO.NewsDTO;
import ba.unsa.etf.nwt.NewsService.DTO.NotificationDTO;
import ba.unsa.etf.nwt.NewsService.model.ErrorMsg;
import ba.unsa.etf.nwt.NewsService.model.News;
import ba.unsa.etf.nwt.NewsService.model.Notification;
import ba.unsa.etf.nwt.NewsService.repositories.NotificationsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class NotificationController {

    private final NotificationsRepository notificationsRepository;
    private final Validator validator;

    public NotificationController(NotificationsRepository notificationsRepository, Validator validator) {
        this.notificationsRepository = notificationsRepository;
        this.validator = validator;
    }

    @GetMapping(value="/notifications")
    public List<Notification> getQuestions() {
        List<Notification> notifications = notificationsRepository.findAll();
        if (notifications.isEmpty()) {
            return Collections.emptyList();
        }
        return notifications;
    }

    @PostMapping(value="/notifications")
    public ResponseEntity<?> createNotification(@RequestBody NotificationDTO notificationDTO) {
        Errors errors = new BeanPropertyBindingResult(notificationDTO, "notificationDTO");
        validator.validate(notificationDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
//            throw new RuntimeException(errorMessage.toString());
        }
        Notification notification = new Notification();
        notification.setTip_notifikacije(notificationDTO.getTip_notifikacije());
        notification.setSadrzaj(notificationDTO.getSadrzaj());
        notification.setUser_uid(notificationDTO.getUser_uid());

        Notification savedNotification = notificationsRepository.save(notification);
        return new ResponseEntity<>(savedNotification, HttpStatus.CREATED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
