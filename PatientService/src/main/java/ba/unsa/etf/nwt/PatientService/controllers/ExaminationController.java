package ba.unsa.etf.nwt.PatientService.controllers;

import ba.unsa.etf.nwt.PatientService.DTO.DiaryEntryDTO;
import ba.unsa.etf.nwt.PatientService.DTO.ExaminationDTO;
import ba.unsa.etf.nwt.PatientService.model.Examination;
import ba.unsa.etf.nwt.PatientService.repositories.ExaminationRepository;
import ba.unsa.etf.nwt.PatientService.services.ExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/examinations")
public class ExaminationController {
    private final ExaminationService examinationService;

    @Autowired
    public ExaminationController(ExaminationService examinationService) {
        this.examinationService = examinationService;
    }

    @GetMapping(value="/")
    public List<ExaminationDTO> getExaminations() {
        return examinationService.getExaminations();
    }

    @PostMapping(value="/")
    public ResponseEntity<?> addExamination(@RequestBody ExaminationDTO examinationDTO){
        return examinationService.addExamination(examinationDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getExamination(@PathVariable("id") Long id){
        return examinationService.getExamination(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteExamination(@PathVariable("id") Long id){
        return examinationService.deleteExamination(id);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateExamination(@PathVariable("id") Long id, @RequestBody ExaminationDTO examinationDTO){
        return examinationService.updateExamination(id, examinationDTO);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateExaminationPartial(@PathVariable("id") Long id, @RequestBody Map<String, Object> fields){
        return examinationService.updateExaminationPartial(id, fields);
    }


}
