package ba.unsa.etf.nwt.PatientService.services;

import ba.unsa.etf.nwt.PatientService.DTO.ReferralDTO;
import ba.unsa.etf.nwt.PatientService.model.ErrorMsg;
import ba.unsa.etf.nwt.PatientService.model.Examination;
import ba.unsa.etf.nwt.PatientService.model.Referral;
import ba.unsa.etf.nwt.PatientService.repositories.ExaminationRepository;
import ba.unsa.etf.nwt.PatientService.repositories.ReferralRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Ref;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ReferralService {
    private final ReferralRepository referralRepository;
    private final Validator validator;

    private final ExaminationRepository examinationRepository;

    public ReferralService(ReferralRepository referralRepository, Validator validator, ExaminationRepository examinationRepository) {
        this.referralRepository = referralRepository;
        this.validator = validator;
        this.examinationRepository = examinationRepository;
    }

    public List<ReferralDTO> getReferrals(){
        List<Referral> referrals = referralRepository.findAll();
        if (referrals.isEmpty()) {
            return Collections.emptyList();
        }
        return referrals.stream().map(this::convertToDTO).toList();
    }

    public ResponseEntity<?> addReferral(ReferralDTO referralDTO) {
        Errors errors = new BeanPropertyBindingResult(referralDTO, "referralDTO");
        validator.validate(referralDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        Examination examination = examinationRepository.findById(referralDTO.getPregled_id()).orElse(null);
        if (examination == null) {
            return new ResponseEntity<>(new ErrorMsg("not found","Nije pronadjen nijedan pregled sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        Referral referral = convertToEntity(referralDTO, examination);
        referral = referralRepository.save(referral);
        return new ResponseEntity<>(referral, HttpStatus.CREATED);
    }


    public ResponseEntity<?> getReferral(Long id) {
        ResponseEntity<?> response = getReferralNotDTO(id);
        if(response.getStatusCode()!=HttpStatus.OK){
            return response;
        }
        Referral referral = (Referral) response.getBody();
        assert referral != null;
        ReferralDTO referralDTO = convertToDTO(referral);
        return new ResponseEntity<>(referralDTO, HttpStatus.OK);
    }

    private ResponseEntity<?> getReferralNotDTO(Long id) {
        Optional<Referral> optionalReferral = referralRepository.findById(id);
        if (optionalReferral.isEmpty()){
            return new ResponseEntity<>(new ErrorMsg("not found", "Nije pronađena nijedna uputnica sa tim ID-em."), HttpStatus.NOT_FOUND);
        }
        Referral referral = optionalReferral.get();
        return new ResponseEntity<>(referral, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteReferral(Long id) {
        ResponseEntity<?> response = getReferral(id);
        if(response.getStatusCode() == HttpStatus.OK) {
            referralRepository.deleteById(id);
        }
        return response;
    }

    public ResponseEntity<?> updateReferral(Long id, ReferralDTO referralDTO) {
        ResponseEntity<?> response = getReferralNotDTO(id);
        if(response.getStatusCode() != HttpStatus.OK) {
            return response;
        }

        Errors errors = new BeanPropertyBindingResult(referralDTO, "referralDTO");
        validator.validate(referralDTO, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            errors.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new ErrorMsg(errorMessage.toString()), HttpStatus.FORBIDDEN);
        }

        Examination examination = examinationRepository.findById(referralDTO.getPregled_id()).orElse(null);
        if (examination == null) {
            return new ResponseEntity<>(new ErrorMsg("not found","Nije pronadjen nijedan pregled sa tim ID-em."), HttpStatus.NOT_FOUND);
        }

        Referral referral = (Referral) response.getBody();
        assert referral != null;
        updateFromDTO(referral, referralDTO, examination);
        referral = referralRepository.save(referral);
        return new ResponseEntity<>(convertToDTO(referral), HttpStatus.OK);
    }

    private Referral updateFromDTO(Referral referral, ReferralDTO referralDTO, Examination pregled){
        referral.setSpecijalista_uid((referralDTO.getSpecijalista_uid()));
        referral.setKomentar(referralDTO.getKomentar());
        referral.setDatum_isteka(referralDTO.getDatum_isteka());
        referral.setPregled(pregled);
        return referral;
    }
    private Referral convertToEntity(ReferralDTO referralDTO, Examination pregled) {
        Referral referral = new Referral();
        return updateFromDTO(referral, referralDTO, pregled);
    }

    private ReferralDTO convertToDTO(Referral referral){
        ReferralDTO referralDTO = new ReferralDTO(
                referral.getPregled().getID(),
                referral.getSpecijalista_uid(),
                referral.getKomentar(),referral.
                getDatum_isteka()
        );
        referralDTO.setID(referral.getID());
        return referralDTO;
    }
}
