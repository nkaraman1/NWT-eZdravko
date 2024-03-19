package ba.unsa.etf.nwt.NewsService.controllers;

import ba.unsa.etf.nwt.NewsService.model.Terapy;
import ba.unsa.etf.nwt.NewsService.repositories.TerapyRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class TerapyController {
    private final TerapyRepository terapyRepository;

    public TerapyController(TerapyRepository terapyRepository) {
        this.terapyRepository = terapyRepository;
    }

    @GetMapping(value="/terapy")
    public List<Terapy> getQuestions() {
        List<Terapy> terapy = terapyRepository.findAll();
        if (terapy.isEmpty()) {
            return Collections.emptyList();
        }
        return terapy;
    }
}
