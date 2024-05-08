package ba.unsa.etf.nwt.NewsService.services;

import ba.unsa.etf.nwt.NewsService.DTO.NewsDTO;
import ba.unsa.etf.nwt.NewsService.DTO.NotificationDTO;
import ba.unsa.etf.nwt.NewsService.controllers.NotificationController;
import ba.unsa.etf.nwt.NewsService.model.ErrorMsg;
import ba.unsa.etf.nwt.NewsService.model.News;
import ba.unsa.etf.nwt.NewsService.model.Notification;
import ba.unsa.etf.nwt.NewsService.repositories.NotificationsRepository;
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
public class NotificationService {
    private final NotificationsRepository notificationsRepository;
    private final Validator validator;

    public NotificationService(NotificationsRepository notificationsRepository, Validator validator) {
        this.notificationsRepository = notificationsRepository;
        this.validator = validator;
    }

    public List<NotificationDTO> getNotifications() {
        List<Notification> notifications = notificationsRepository.findAll();
        if (notifications.isEmpty()) {
            return Collections.emptyList();
        }
        return notifications.stream().map(this::convertToDTO).toList();
    }

    public ResponseEntity<?> createNotification(NotificationDTO notificationDTO) {
        Errors errors = new BeanPropertyBindingResult(notificationDTO, "notificationDTO");
        validator.validate(notificationDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
//            throw new RuntimeException(errorMessage.toString());
        }

        Notification notification = convertToEntity(notificationDTO);
        notification = notificationsRepository.save(notification);
        return new ResponseEntity<>(convertToDTO(notification), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getNotificationByID(Long id) {
        ResponseEntity<?> response = getNotificationByIDNotDTO(id);
        if (response.getStatusCode() != HttpStatus.OK) {
            return response;
        }
        Notification notification = (Notification) response.getBody();
        assert notification != null;
        NotificationDTO notificationDTO = convertToDTO(notification);
        return new ResponseEntity<>(notificationDTO, HttpStatus.OK);
    }

    private ResponseEntity<?> getNotificationByIDNotDTO(Long id) {
        Optional<Notification> optionalNotification = notificationsRepository.findById(id);
        if(optionalNotification.isEmpty()) {
            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronadjena notifikacija sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        Notification notification = optionalNotification.get();
        return new ResponseEntity<>(notification, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteNotification(Long id) {
        ResponseEntity<?> response = getNotificationByIDNotDTO(id);
        if(response.getStatusCode() == HttpStatus.OK){
            Notification notification = (Notification) response.getBody();
            notificationsRepository.deleteById(id);
        }
        return response;
    }

    public ResponseEntity<?> updateNotification(Long id, NotificationDTO notificationDTO){
        ResponseEntity<?> response = getNotificationByIDNotDTO(id);
        if(response.getStatusCode() != HttpStatus.OK){
            return response;
        }

        Notification notification = (Notification) response.getBody();

        Errors errors = new BeanPropertyBindingResult(notificationDTO, "notificationDTO");
        validator.validate(notificationDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        assert notification != null;
        updateFromDTO(notification, notificationDTO);
        notification = notificationsRepository.save(notification);

        return new ResponseEntity<>(convertToDTO(notification), HttpStatus.OK);
    }

    public ResponseEntity<?> updateNotificationPartial(@PathVariable("id") Long id, @RequestBody Map<String, Object> fields){
        Optional<Notification> optionalNotification = notificationsRepository.findById(id);
        if (!optionalNotification.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena notifikacija sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        AtomicBoolean error = new AtomicBoolean(false);
        StringBuilder errorMessage = new StringBuilder();
        fields.forEach((key, value)->{
            Field field = ReflectionUtils.findField(Notification.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, optionalNotification.get(), value);
            }else{
                error.set(true);
                errorMessage.append("Notifikacija nema polje ").append(key).append(". ");
            }
        });
        if(error.get()){
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }
        Notification notification = notificationsRepository.save(optionalNotification.get());

        return new ResponseEntity<>(convertToDTO(notification), HttpStatus.OK);
    }

    private Notification updateFromDTO(Notification notification, NotificationDTO notificationDTO) {
        notification.setTip_notifikacije(notificationDTO.getTip_notifikacije());
        notification.setSadrzaj(notificationDTO.getSadrzaj());
        notification.setUser_uid(notificationDTO.getUser_uid());
        return notification;
    }

    private Notification convertToEntity(NotificationDTO notificationDTO){
        Notification notification = new Notification();
        return updateFromDTO(notification, notificationDTO);
    }

    private NotificationDTO convertToDTO(Notification notification){
        NotificationDTO notificationDTO = new NotificationDTO(
                notification.getTip_notifikacije(),
                notification.getSadrzaj(),
                notification.getUser_uid()
        );

        return notificationDTO;
    }
}
