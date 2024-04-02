package ba.unsa.etf.nwt.PatientService.controllers;

import ba.unsa.etf.nwt.PatientService.DTO.ReferralDTO;
import ba.unsa.etf.nwt.PatientService.model.Referral;
import ba.unsa.etf.nwt.PatientService.services.ReferralService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("api/referrals")
@RestController
public class ReferralController {

    private final ReferralService referralService;

    public ReferralController (ReferralService referralService){
        this.referralService = referralService;
    }

    @GetMapping(value="/")
    public List<Referral> getReferrals(){
        return referralService.getReferrals();
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> addReferral(@RequestBody ReferralDTO referralDTO){
        return referralService.addReferral(referralDTO);
    }
}
