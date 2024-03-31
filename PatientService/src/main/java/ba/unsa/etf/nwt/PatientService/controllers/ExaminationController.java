package ba.unsa.etf.nwt.PatientService.controllers;

import ba.unsa.etf.nwt.PatientService.DTO.ExaminationDTO;
import ba.unsa.etf.nwt.PatientService.model.Examination;
import ba.unsa.etf.nwt.PatientService.repositories.ExaminationRepository;
import ba.unsa.etf.nwt.PatientService.services.ExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "/api/examinations")
public class ExaminationController {
    private final ExaminationService examinationService;

    @Autowired
    public ExaminationController(ExaminationService examinationService) {
        this.examinationService = examinationService;
    }

    @GetMapping(value="/")
    public List<Examination> getDiaryEntries() {
        return examinationService.getDiaryEntries();
    }

    @PostMapping(value="/")
    public ResponseEntity<?> addExamination(@RequestBody ExaminationDTO examinationDTO){
        return examinationService.addExamination(examinationDTO);
    }
}
