package ba.unsa.etf.nwt.PatientService.controllers;

import ba.unsa.etf.nwt.PatientService.DTO.ReferralDTO;
import ba.unsa.etf.nwt.PatientService.model.Referral;
import ba.unsa.etf.nwt.PatientService.services.ReferralService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/referrals")
@RestController
public class ReferralController {

    private final ReferralService referralService;

    public ReferralController (ReferralService referralService){
        this.referralService = referralService;
    }

    @GetMapping(value="/")
    public List<ReferralDTO> getReferrals(){
        return referralService.getReferrals();
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> addReferral(@RequestBody ReferralDTO referralDTO){
        return referralService.addReferral(referralDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getReferral(@PathVariable("id") Long id){
        return referralService.getReferral(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteReferral(@PathVariable("id") Long id){
        return referralService.deleteReferral(id);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<?> updateReferral(@PathVariable("id") Long id, @RequestBody ReferralDTO referralDTO){
        return referralService.updateReferral(id, referralDTO);
    }
}
