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

import java.util.Collections;
import java.util.List;

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

    public List<Referral> getReferrals(){
        List<Referral> referrals = referralRepository.findAll();
        if (referrals.isEmpty()) {
            return Collections.emptyList();
        }
        return referrals;
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
            return new ResponseEntity<>(new ErrorMsg("Nije pronadjen nijedan pregled sa tim ID-em."), HttpStatus.FORBIDDEN);
        }

        Referral referral = convertToEntity(referralDTO, examination);
        referral = referralRepository.save(referral);
        return new ResponseEntity<>(referral, HttpStatus.CREATED);
    }

    private Referral convertToEntity(ReferralDTO referralDTO, Examination pregled) {
        Referral referral = new Referral();
        referral.setSpecijalista_uid((referralDTO.getSpecijalista_uid()));
        referral.setKomentar(referral.getKomentar());
        referral.setDatum_isteka(referralDTO.getDatum_isteka());
        referral.setPregled(pregled);
        return  referral;
    }
}
