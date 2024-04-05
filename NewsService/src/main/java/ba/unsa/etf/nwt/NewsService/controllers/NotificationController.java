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
import java.util.Optional;

@RestController
public class NotificationController {

    private final NotificationsRepository notificationsRepository;
    private final Validator validator;

    public NotificationController(NotificationsRepository notificationsRepository, Validator validator) {
        this.notificationsRepository = notificationsRepository;
        this.validator = validator;
    }

    @GetMapping(value="/notifications")
    public List<Notification> getNotifications() {
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

    @GetMapping(value = "/notifications/{id}")
    public ResponseEntity<?> getNotificationByID(@PathVariable Long id) {
        Optional<Notification> notification = notificationsRepository.findById(id);
        if (notification.isPresent()) {
            return new ResponseEntity<>(notification.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena notifikacija sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value="/notifications/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        Optional<Notification> notification = notificationsRepository.findById(id);
        if (notification.isPresent()) {
            notificationsRepository.deleteById(id);
            return new ResponseEntity<>(notification.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena notifikacija sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value="/notifications/{id}")
    public ResponseEntity<?> updateNotification(@PathVariable Long id, @RequestBody NotificationDTO notificationDTO){
        Optional<Notification> optionalNotification = notificationsRepository.findById(id);
        if (!optionalNotification.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena nijedna notifikacija sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        Errors errors = new BeanPropertyBindingResult(notificationDTO, "notificationDTO");
        validator.validate(notificationDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
//            throw new RuntimeException(errorMessage.toString());
        }

        Notification existingNotification = optionalNotification.get();

        existingNotification.setUser_uid(notificationDTO.getUser_uid());
        existingNotification.setSadrzaj(notificationDTO.getSadrzaj());
        existingNotification.setTip_notifikacije(notificationDTO.getTip_notifikacije());

        Notification updatedNotification = notificationsRepository.save(existingNotification);

        return new ResponseEntity<>(updatedNotification, HttpStatus.OK);
    }

    @PatchMapping(value="/notifications/{id}")
    public ResponseEntity<?> patchNotifications(@PathVariable Long id, @RequestBody NotificationDTO notificationDTO) {
        Optional<Notification> optionalNotification = notificationsRepository.findById(id);
        if (!optionalNotification.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena nijedna notifikacija sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

//        Errors errors = new BeanPropertyBindingResult(notificationDTO, "notificationDTO");
//        validator.validate(notificationDTO, errors);
//
//        if (errors.hasErrors()) {
//            StringBuilder errorMessage = new StringBuilder();
//            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
//            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
////            throw new RuntimeException(errorMessage.toString());
//        }

        Notification existingNotification = optionalNotification.get();

        // Update only the fields that are present in the request
        if (notificationDTO.getSadrzaj() != null) {
            existingNotification.setSadrzaj(notificationDTO.getSadrzaj());
        }
        if(notificationDTO.getUser_uid() != null){
            existingNotification.setUser_uid(notificationDTO.getUser_uid());
        }
        if(notificationDTO.getTip_notifikacije() != null){
            existingNotification.setTip_notifikacije(notificationDTO.getTip_notifikacije());
        }

        // Save the updated news
        Notification updatedNotification = notificationsRepository.save(existingNotification);

        return new ResponseEntity<>(updatedNotification, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
