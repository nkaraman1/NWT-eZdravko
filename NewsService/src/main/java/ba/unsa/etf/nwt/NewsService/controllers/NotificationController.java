package ba.unsa.etf.nwt.NewsService.controllers;

import ba.unsa.etf.nwt.NewsService.DTO.NewsDTO;
import ba.unsa.etf.nwt.NewsService.DTO.NotificationDTO;
import ba.unsa.etf.nwt.NewsService.model.ErrorMsg;
import ba.unsa.etf.nwt.NewsService.model.News;
import ba.unsa.etf.nwt.NewsService.model.Notification;
import ba.unsa.etf.nwt.NewsService.repositories.NotificationsRepository;
import ba.unsa.etf.nwt.NewsService.services.NotificationService;
import org.aspectj.weaver.ast.Not;
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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Map;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(value="/notifications")
    public List<NotificationDTO> getNotifications() {
        return notificationService.getNotifications();
    }

    @PostMapping(value="/notifications")
    public ResponseEntity<?> createNotification(@RequestBody NotificationDTO notificationDTO) {
        return notificationService.createNotification(notificationDTO);
    }

    @GetMapping(value = "/notifications/{id}")
    public ResponseEntity<?> getNotificationByID(@PathVariable Long id) {
        return notificationService.getNotificationByID(id);
    }

    @GetMapping(value = "/notifications/uid/{uid}")
    public ResponseEntity<?> getNotificationByUID(@PathVariable String uid) {
        return notificationService.getNotificationByUID(uid);
    }

    @DeleteMapping(value="/notifications/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        return notificationService.deleteNotification(id);
    }

    @PutMapping(value="/notifications/{id}")
    public ResponseEntity<?> updateNotification(@PathVariable Long id, @RequestBody NotificationDTO notificationDTO){
        return notificationService.updateNotification(id, notificationDTO);
    }

    @PatchMapping(value = "/notifications/{id}")
    public ResponseEntity<?> updateNotificationPartial(@PathVariable("id") Long id, @RequestBody Map<String, Object> fields){
        return notificationService.updateNotificationPartial(id, fields);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
