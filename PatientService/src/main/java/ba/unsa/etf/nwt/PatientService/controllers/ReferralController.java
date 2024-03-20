package ba.unsa.etf.nwt.PatientService.controllers;

import ba.unsa.etf.nwt.PatientService.model.Referral;
import ba.unsa.etf.nwt.PatientService.repositories.ReferralRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class ReferralController {

    private final ReferralRepository referralRepository;

    public ReferralController (ReferralRepository referralRepository){
        this.referralRepository = referralRepository;
    }

    @GetMapping(value="/referrals")
    public List<Referral> getReferrals(){
        List<Referral> referrals = referralRepository.findAll();
        if (referrals.isEmpty()) {
            return Collections.emptyList();
        }
        return referrals;
    }
}
