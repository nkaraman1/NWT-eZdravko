package ba.unsa.etf.nwt.NewsService.services;

import ba.unsa.etf.nwt.NewsService.DTO.NewsDTO;
import ba.unsa.etf.nwt.NewsService.DTO.NotificationDTO;
import ba.unsa.etf.nwt.NewsService.DTO.TherapyDTO;
import ba.unsa.etf.nwt.NewsService.feign.NotificationInterface;
import ba.unsa.etf.nwt.NewsService.feign.UserInterface;
import ba.unsa.etf.nwt.NewsService.model.ErrorMsg;
import ba.unsa.etf.nwt.NewsService.model.News;
import ba.unsa.etf.nwt.NewsService.model.Therapy;
import ba.unsa.etf.nwt.NewsService.repositories.TherapyRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TherapyService {
    private final TherapyRepository therapyRepository;
    private final Validator validator;

    @Autowired
    private NotificationInterface notificationInterface;
    @Autowired
    private UserInterface userInterface;

    @Autowired
    public TherapyService(TherapyRepository therapyRepository, Validator validator) {
        this.therapyRepository = therapyRepository;
        this.validator = validator;
    }

    public List<TherapyDTO> getTherapies() {
        List<Therapy> therapy = therapyRepository.findAll();
        if (therapy.isEmpty()) {
            return Collections.emptyList();
        }
        return therapy.stream().map(this::convertToDTO).toList();
    }

    public ResponseEntity<?> createTherapy(TherapyDTO therapyDTO) {
        Errors errors = new BeanPropertyBindingResult(therapyDTO, "therapyDTO");
        validator.validate(therapyDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        Therapy therapy = convertToEntity(therapyDTO);
        therapy = therapyRepository.save(therapy);
        NotificationDTO newNotification = new NotificationDTO("therapy", "Vaša terapija je spremna: " + therapy.getLijek() + therapy.getKolicina(), therapy.getDoktor_uid());
        notificationInterface.createNotification(newNotification);
        userInterface.getUserByUID(therapy.getDoktor_uid());
        return new ResponseEntity<>(convertToDTO(therapy), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getTherapyByID(Long id) {
        ResponseEntity<?> response = getTherapyByIDNotDTO(id);
        if (response.getStatusCode() != HttpStatus.OK) {
            return response;
        }
        Therapy therapy = (Therapy) response.getBody();
        assert therapy != null;
        TherapyDTO therapyDTO = convertToDTO(therapy);
        return new ResponseEntity<>(therapyDTO, HttpStatus.OK);
    }

    private ResponseEntity<?> getTherapyByIDNotDTO(Long id) {
        Optional<Therapy> optionalTherapy = therapyRepository.findById(id);
        if(optionalTherapy.isEmpty()) {
            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronadjena terapija sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        Therapy therapy = optionalTherapy.get();
        return new ResponseEntity<>(therapy, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteTherapy(Long id) {
        ResponseEntity<?> response = getTherapyByIDNotDTO(id);
        if(response.getStatusCode() == HttpStatus.OK){
            therapyRepository.deleteById(id);
        }
        return response;
    }

    public ResponseEntity<?> updateTherapy(Long id, TherapyDTO therapyDTO){
        ResponseEntity<?> response = getTherapyByIDNotDTO(id);
        if(response.getStatusCode() != HttpStatus.OK){
            return response;
        }

        Therapy therapy = (Therapy) response.getBody();

        Errors errors = new BeanPropertyBindingResult(therapyDTO, "therapyDTO");
        validator.validate(therapyDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()));
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        assert therapy != null;
        updateFromDTO(therapy, therapyDTO);
        therapy = therapyRepository.save(therapy);
        NotificationDTO newNotification = new NotificationDTO("therapy", "Vaša terapija je izmijenjena!", therapy.getDoktor_uid());
        notificationInterface.createNotification(newNotification);
        userInterface.getUserByUID(therapy.getDoktor_uid());

        return new ResponseEntity<>(convertToDTO(therapy), HttpStatus.OK);
    }

    public ResponseEntity<?> updateTherapyPartial(Long id, Map<String, Object> fields){
        Optional<Therapy> optionalTherapy = therapyRepository.findById(id);
        if (!optionalTherapy.isPresent()) {
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjena terapija sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        AtomicBoolean error = new AtomicBoolean(false);
        StringBuilder errorMessage = new StringBuilder();
        fields.forEach((key, value)->{
            Field field = ReflectionUtils.findField(Therapy.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, optionalTherapy.get(), value);
            }else{
                error.set(true);
                errorMessage.append("Terapija nema polje ").append(key).append(". ");
            }
        });
        if(error.get()){
            return new ResponseEntity<>(new ErrorMsg("validation", errorMessage.toString()), HttpStatus.FORBIDDEN);
        }
        Therapy therapy = therapyRepository.save(optionalTherapy.get());
        NotificationDTO newNotification = new NotificationDTO("therapy", "Vaša terapija je izmijenjena!", therapy.getDoktor_uid());
        notificationInterface.createNotification(newNotification);
        userInterface.getUserByUID(therapy.getDoktor_uid());
        return new ResponseEntity<>(convertToDTO(therapy), HttpStatus.OK);
    }

    private Therapy updateFromDTO(Therapy therapy, TherapyDTO therapyDTO) {
        therapy.setLijek(therapyDTO.getLijek());
        therapy.setKolicina(therapyDTO.getKolicina());
        therapy.setNapomena(therapyDTO.getNapomena());
        therapy.setPacijent_uid(therapyDTO.getPacijent_uid());
        therapy.setDoktor_uid(therapyDTO.getDoktor_uid());
        return therapy;
    }

    private Therapy convertToEntity(TherapyDTO therapyDTO){
        Therapy therapy = new Therapy();
        return updateFromDTO(therapy, therapyDTO);
    }

    private TherapyDTO convertToDTO(Therapy therapy){
        TherapyDTO therapyDTO = new TherapyDTO(
                therapy.getLijek(),
                therapy.getNapomena(),
                therapy.getKolicina(),
                therapy.getPacijent_uid(),
                therapy.getDoktor_uid()
        );

        return therapyDTO;
    }

}
