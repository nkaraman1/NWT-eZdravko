package ba.unsa.etf.nwt.PatientService.services;

import ba.unsa.etf.nwt.PatientService.DTO.ExaminationDTO;
import ba.unsa.etf.nwt.PatientService.model.DiaryEntry;
import ba.unsa.etf.nwt.PatientService.model.ErrorMsg;
import ba.unsa.etf.nwt.PatientService.model.Examination;
import ba.unsa.etf.nwt.PatientService.model.Referral;
import ba.unsa.etf.nwt.PatientService.repositories.ExaminationRepository;
import ba.unsa.etf.nwt.PatientService.repositories.ReferralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ExaminationService {

    private final ExaminationRepository examinationRepository;

    private final ReferralRepository referralRepository;
    private final Validator validator;

    @Autowired
    public ExaminationService(ExaminationRepository examinationRepository, ReferralRepository referralRepository, Validator validator) {
        this.examinationRepository = examinationRepository;
        this.referralRepository = referralRepository;
        this.validator = validator;
    }

    public List<ExaminationDTO> getExaminations() {
        List<Examination> examinations = examinationRepository.findAll();
        if (examinations.isEmpty()) {
            return Collections.emptyList();
        }
        return examinations.stream().map(this::convertToDTO).toList();
    }

    public ResponseEntity<?> addExamination(ExaminationDTO examinationDTO) {
        Errors errors = new BeanPropertyBindingResult(examinationDTO, "examinationDTO");
        validator.validate(examinationDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        Examination examination = convertToEntity(examinationDTO);
        examination = examinationRepository.save(examination);
        return new ResponseEntity<>(examination, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getExamination(Long id) {
        ResponseEntity<?> response = getExaminationNotDTO(id);
        if(response.getStatusCode()!=HttpStatus.OK){
            return response;
        }
        Examination examination = (Examination) response.getBody();
        assert examination != null;
        ExaminationDTO examinationDTO = convertToDTO(examination);
        return new ResponseEntity<>(examinationDTO, HttpStatus.OK);
    }

    private ResponseEntity<?> getExaminationNotDTO(Long id){
        Optional<Examination> optionalExamination = examinationRepository.findById(id);
        if (optionalExamination.isEmpty()){
            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronađen pregled sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        Examination examination = optionalExamination.get();
        return new ResponseEntity<>(examination, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteExamination(Long id) {
        ResponseEntity<?> response = getExamination(id);
        if(response.getStatusCode() != HttpStatus.OK) {
            return response;
        };
        examinationRepository.deleteById(id);
        return response;
    }

    public ResponseEntity<?> updateExamination(Long id, ExaminationDTO examinationDTO) {
        Errors errors = new BeanPropertyBindingResult(examinationDTO, "examinationDTO");
        validator.validate(examinationDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        ResponseEntity<?> response = getExaminationNotDTO(id);
        if(response.getStatusCode() != HttpStatus.OK) {
            return response;
        }
        Examination examination = (Examination) response.getBody();
        assert examination != null;
        updateFromDTO(examination, examinationDTO);
        examination = examinationRepository.save(examination);
        return new ResponseEntity<>(convertToDTO(examination), HttpStatus.OK);
    }



    private Examination convertToEntity(ExaminationDTO examinationDTO) {
        Examination examination = new Examination();
        return updateFromDTO(examination, examinationDTO);
    }

    private Examination updateFromDTO(Examination examination, ExaminationDTO examinationDTO){
        examination.setDijagnoza(examinationDTO.getDijagnoza());
        examination.setDoktor_uid(examinationDTO.getDoktor_uid());
        examination.setPacijent_uid(examinationDTO.getPacijent_uid());
        examination.setTermin_pregleda(examinationDTO.getTermin_pregleda());

        return examination;
    }

    private ExaminationDTO convertToDTO(Examination examination){
        ExaminationDTO examinationDTO = new ExaminationDTO(
                examination.getPacijent_uid(),
                examination.getDoktor_uid(),
                examination.getDijagnoza(),
                examination.getTermin_pregleda()
        );
        examinationDTO.setUputnice(examination.getUputnice().stream().map(Referral::getID).toList());
        examinationDTO.setID(examination.getID());
        return examinationDTO;
    }


}
