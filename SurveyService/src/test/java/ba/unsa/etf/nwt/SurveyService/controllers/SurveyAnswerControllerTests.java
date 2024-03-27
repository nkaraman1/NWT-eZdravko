package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.DTO.SurveyAnswerDTO;
import ba.unsa.etf.nwt.SurveyService.controllers.SurveyAnswerController;
import ba.unsa.etf.nwt.SurveyService.model.AnswerOptions;
import ba.unsa.etf.nwt.SurveyService.model.SurveyAnswer;
import ba.unsa.etf.nwt.SurveyService.repositories.AnswerOptionsRepository;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyAnswerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SurveyAnswerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SurveyAnswerRepository surveyAnswerRepository;

    @MockBean
    private AnswerOptionsRepository answerOptionsRepository;

    @Test
    public void createSurveyAnswer_ValidInput_Success() throws Exception {
        // Mock SurveyAnswerDTO object
        SurveyAnswerDTO surveyAnswerDTO = new SurveyAnswerDTO();
        surveyAnswerDTO.setAnswerId(1L); // Assuming there is an answer with ID 1 in the database

        // Mock AnswerOptions object
        AnswerOptions answerOptions = new AnswerOptions();
        answerOptions.setID(1L);

        // Mock the behavior of AnswerOptionsRepository to return a mock AnswerOptions object
        when(answerOptionsRepository.findById(1L)).thenReturn(Optional.of(answerOptions));

        // Perform POST request to create a new survey answer
        mockMvc.perform(post("/surveyanswers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(surveyAnswerDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void createSurveyAnswer_InvalidInput_Fail() throws Exception {
        // Create a SurveyAnswerDTO with invalid data (e.g., missing required fields)
        SurveyAnswerDTO surveyAnswerDTO = new SurveyAnswerDTO();

        // Perform POST request to create a new survey answer
        mockMvc.perform(post("/surveyanswers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(surveyAnswerDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createSurveyAnswer_AnswerOptionsNotFound_Fail() throws Exception {
        // Mock SurveyAnswerDTO object with a non-existent answer ID
        SurveyAnswerDTO surveyAnswerDTO = new SurveyAnswerDTO();
        surveyAnswerDTO.setAnswerId(999L); // Assuming there is no answer with ID 999 in the database

        // Mock the behavior of AnswerOptionsRepository to return an empty optional, indicating answer not found
        when(answerOptionsRepository.findById(999L)).thenReturn(Optional.empty());

        // Perform POST request to create a new survey answer
        mockMvc.perform(post("/surveyanswers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(surveyAnswerDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getSurveyAnswerById_ValidId_ReturnsSurveyAnswer() throws Exception {
        // Arrange
        SurveyAnswer surveyAnswer = new SurveyAnswer();
        surveyAnswer.setID(1L);

        Mockito.when(surveyAnswerRepository.findById(1L)).thenReturn(Optional.of(surveyAnswer));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/surveyanswers/surveyanswer/id/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

//    @Test
//    public void updateSurveyAnswer_ValidId_ReturnsUpdatedSurveyAnswer() throws Exception {
//        // Arrange
//        SurveyAnswerDTO surveyAnswerDTO = new SurveyAnswerDTO();
//        surveyAnswerDTO.setAnswerId(1L);
//        AnswerOptions answerOptions = new AnswerOptions();
//        answerOptions.setID(1L);
//
//        Mockito.when(answerOptionsRepository.findById(1L)).thenReturn(Optional.of(answerOptions));
//
//        SurveyAnswer surveyAnswer = new SurveyAnswer();
//        surveyAnswer.setID(1L);
//        surveyAnswer.setAnketaOdgovor(answerOptions);
//
//        Mockito.when(surveyAnswerRepository.findById(1L)).thenReturn(Optional.of(surveyAnswer));
//        Mockito.when(surveyAnswerRepository.save(any(SurveyAnswer.class))).thenReturn(surveyAnswer);
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders.put("/surveyanswers/surveyanswer/{id}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(surveyAnswerDTO)))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
//    }

    @Test
    public void deleteSurveyAnswer_ValidId_DeletesSurveyAnswer() throws Exception {
        // Arrange
        SurveyAnswer surveyAnswer = new SurveyAnswer();
        surveyAnswer.setID(1L);

        Mockito.when(surveyAnswerRepository.findById(1L)).thenReturn(Optional.of(surveyAnswer));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/surveyanswers/surveyanswer/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    // Utility method to convert objects to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}