package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.DTO.SurveyAnswerDTO;
import ba.unsa.etf.nwt.SurveyService.model.ErrorMsg;
import ba.unsa.etf.nwt.SurveyService.model.SurveyAnswer;
import ba.unsa.etf.nwt.SurveyService.model.AnswerOptions;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyAnswerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SurveyAnswerControllerTests {

    @Mock
    private SurveyAnswerRepository surveyAnswerRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private SurveyAnswerController surveyAnswerController;
    @Test
    void createSurveyAnswer_Success() {
        // Mock DTO
        SurveyAnswerDTO surveyAnswerDTO = new SurveyAnswerDTO();
        surveyAnswerDTO.setAnswerId(1L);

        // Mock validation result
        doNothing().when(validator).validate(any(), any());
//        when(validator.validate(any(), any())).thenReturn(new BeanPropertyBindingResult(surveyAnswerDTO, "surveyAnswerDTO"));

        // Mock repository behavior
        AnswerOptions answerOptions = new AnswerOptions(1L, "Answer Content");
        when(surveyAnswerRepository.findById(1)).thenReturn(Optional.of(new SurveyAnswer(1L, answerOptions)));
        when(surveyAnswerRepository.save(any(SurveyAnswer.class))).thenReturn(new SurveyAnswer(1L, answerOptions));

        // Create controller instance
        SurveyAnswerController controller = new SurveyAnswerController(surveyAnswerRepository, validator);

        // Perform POST request
        ResponseEntity<?> response = controller.createSurveyAnswer(surveyAnswerDTO);

        // Verify response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, ((SurveyAnswer) response.getBody()).getID());
    }

//    @Test
//    public void createSurveyAnswer_AnswerNotFound() {
//        // Mock validation result
//        doNothing().when(validator).validate(any(), any());
//
//        // Mock repository behavior
//        when(surveyAnswerRepository.findById(anyInt())).thenReturn(null);
//
//        // Create a test DTO
//        SurveyAnswerDTO surveyAnswerDTO = new SurveyAnswerDTO();
//        surveyAnswerDTO.setAnswerId(1L); // Assuming ID 1 doesn't exist
//
//        // Call the controller method
//        ResponseEntity<?> responseEntity = surveyAnswerController.createSurveyAnswer(surveyAnswerDTO);
//
//        // Assert the response
//        assert responseEntity.getStatusCode() == HttpStatus.FORBIDDEN;
//        assert responseEntity.getBody() != null;
//        assert responseEntity.getBody().equals(new ErrorMsg("Nije pronadjen nijedan ponudjeni odgovor sa tim ID-em."));
//    }
}
