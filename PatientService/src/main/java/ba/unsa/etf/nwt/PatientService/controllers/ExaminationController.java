package ba.unsa.etf.nwt.PatientService.controllers;

import ba.unsa.etf.nwt.PatientService.model.Examination;
import ba.unsa.etf.nwt.PatientService.repositories.ExaminationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class ExaminationController {
    private final ExaminationRepository examinationRepository;

    public ExaminationController(ExaminationRepository examinationRepository) {
        this.examinationRepository = examinationRepository;
    }

    @GetMapping(value="/examinations")
    public List<Examination> getDiaryEntries() {
        List<Examination> examinations = examinationRepository.findAll();
        if (examinations.isEmpty()) {
            return Collections.emptyList();
        }
        return examinations;
    }
}
