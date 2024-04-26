package ba.unsa.etf.nwt.NewsService.controllers;

import ba.unsa.etf.nwt.NewsService.DTO.NotificationDTO;
import ba.unsa.etf.nwt.NewsService.DTO.TherapyDTO;
import ba.unsa.etf.nwt.NewsService.model.ErrorMsg;
import ba.unsa.etf.nwt.NewsService.model.Notification;
import ba.unsa.etf.nwt.NewsService.model.Therapy;
import ba.unsa.etf.nwt.NewsService.repositories.TherapyRepository;
import ba.unsa.etf.nwt.NewsService.services.TherapyService;
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
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
public class TherapyController {
    private final TherapyService therapyService;

    @Autowired
    public TherapyController(TherapyService therapyService) {
        this.therapyService = therapyService;
    }
    @GetMapping(value="/therapy")
    public List<TherapyDTO> getTherapies() {
        return therapyService.getTherapies();
    }

    @PostMapping(value="/therapy")
    public ResponseEntity<?> createTherapy(@RequestBody TherapyDTO therapyDTO) {
        return therapyService.createTherapy(therapyDTO);
    }

    @GetMapping(value = "/therapy/{id}")
    public ResponseEntity<?> getTherapyByID(@PathVariable Long id) {
        return therapyService.getTherapyByID(id);
    }

    @DeleteMapping(value = "/therapy/{id}")
    public ResponseEntity<?> deleteTherapy(@PathVariable Long id) {
        return therapyService.deleteTherapy(id);
    }

    @PutMapping(value="/therapy/{id}")
    public ResponseEntity<?> updateTherapy(@PathVariable Long id, @RequestBody TherapyDTO therapyDTO){
        return therapyService.updateTherapy(id, therapyDTO);
    }

    @PatchMapping(value = "/therapy/{id}")
    public ResponseEntity<?> updateTherapyPartial(@PathVariable("id") Long id, @RequestBody Map<String, Object> fields){
        return therapyService.updateTherapyPartial(id, fields);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
