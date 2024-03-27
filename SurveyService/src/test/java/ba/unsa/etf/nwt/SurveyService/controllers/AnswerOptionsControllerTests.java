//package ba.unsa.etf.nwt.SurveyService.controllers;
//
//import ba.unsa.etf.nwt.SurveyService.controllers.AnswerOptionsController;
//import ba.unsa.etf.nwt.SurveyService.DTO.AnswerOptionsDTO;
//import ba.unsa.etf.nwt.SurveyService.model.AnswerOptions;
//import ba.unsa.etf.nwt.SurveyService.model.ErrorMsg;
//import ba.unsa.etf.nwt.SurveyService.model.SurveyQuestion;
//import ba.unsa.etf.nwt.SurveyService.repositories.AnswerOptionsRepository;
//import ba.unsa.etf.nwt.SurveyService.repositories.SurveyQuestionRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BeanPropertyBindingResult;
//import org.springframework.validation.Errors;
//import org.springframework.validation.Validator;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class AnswerOptionsControllerTests {
//
//    @Mock
//    private AnswerOptionsRepository answerOptionsRepository;
//
//    @Mock
//    private SurveyQuestionRepository surveyQuestionRepository;
//
//    @Mock
//    private Validator validator;
//
//    @Test
//    public void createAnswerOptions_Success() {
//        // Mock validation result
//        doNothing().when(validator).validate(any(), any());
//
//        // Mock request body
//        AnswerOptionsDTO answerOptionsDTO = new AnswerOptionsDTO();
//        answerOptionsDTO.setQuestionId(1L);
//        answerOptionsDTO.setSadrzaj("Test Answer");
//
//        // Mock SurveyQuestion
//        SurveyQuestion surveyQuestion = new SurveyQuestion();
//        surveyQuestion.setID(1L);
//        when(surveyQuestionRepository.findById(1)).thenReturn(Optional.of(surveyQuestion));
//
//        // Create AnswerOptionsController instance
//        AnswerOptionsController controller = new AnswerOptionsController(answerOptionsRepository, surveyQuestionRepository, validator);
//
//        // Call the controller method
//        ResponseEntity<?> response = controller.createAnswerOptions(answerOptionsDTO);
//
//        // Verify the response
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertNotNull(response.getBody());
//
//        // Verify that repository save method was called
//        verify(answerOptionsRepository, times(1)).save(any(AnswerOptions.class));
//    }
//
//    @Test
//    public void createAnswerOptions_ValidationFailed() {
//        // Mock validation result
//        doNothing().when(validator).validate(any(), any());
//
//        // Mock request body
//        AnswerOptionsDTO answerOptionsDTO = new AnswerOptionsDTO();
//        answerOptionsDTO.setQuestionId(1L);
//        answerOptionsDTO.setSadrzaj("Test Answer");
//
//        // Mock SurveyQuestion
//        SurveyQuestion surveyQuestion = new SurveyQuestion();
//        surveyQuestion.setID(1L);
//        when(surveyQuestionRepository.findById(1)).thenReturn(Optional.of(surveyQuestion));
//
//        // Mock validation errors
//        Errors errors = new BeanPropertyBindingResult(answerOptionsDTO, "answerOptionsDTO");
//        errors.rejectValue("sadrzaj", "invalid", "Validation error message");
//
//        // Mock validator behavior
//        doThrow(new RuntimeException("Validation error message")).when(validator).validate(answerOptionsDTO, errors);
//
//        // Create AnswerOptionsController instance
//        AnswerOptionsController controller = new AnswerOptionsController(answerOptionsRepository, surveyQuestionRepository, validator);
//
//        // Call the controller method
//        ResponseEntity<?> response = controller.createAnswerOptions(answerOptionsDTO);
//
//        // Verify the response
//        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
//        assertEquals("Validation error message", ((ErrorMsg) response.getBody()).getError());
//
//        // Verify that repository methods were not called
//        verifyNoInteractions(answerOptionsRepository);
//    }
//
//    @Test
//    public void createAnswerOptions_SurveyQuestionNotFound() {
//        // Mock validation result
//        doNothing().when(validator).validate(any(), any());
//
//        // Mock request body
//        AnswerOptionsDTO answerOptionsDTO = new AnswerOptionsDTO();
//        answerOptionsDTO.setQuestionId(1L);
//        answerOptionsDTO.setSadrzaj("Test Answer");
//
//        // Mock SurveyQuestion
//        when(surveyQuestionRepository.findById(1)).thenReturn(Optional.empty());
//
//        // Create AnswerOptionsController instance
//        AnswerOptionsController controller = new AnswerOptionsController(answerOptionsRepository, surveyQuestionRepository, validator);
//
//        // Call the controller method
//        ResponseEntity<?> response = controller.createAnswerOptions(answerOptionsDTO);
//
//        // Verify the response
//        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
//        assertEquals("Nije pronadjeno nijedno pitanje u anketi sa tim ID-em.", ((ErrorMsg) response.getBody()).getError());
//
//        // Verify that repository methods were not called
//        verifyNoInteractions(answerOptionsRepository);
//    }
//}
