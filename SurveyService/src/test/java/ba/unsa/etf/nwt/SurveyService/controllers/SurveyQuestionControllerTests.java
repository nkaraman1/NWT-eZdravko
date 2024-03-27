package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.DTO.SurveyQuestionDTO;
import ba.unsa.etf.nwt.SurveyService.model.Survey;
import ba.unsa.etf.nwt.SurveyService.model.SurveyQuestion;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyQuestionRepository;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyRepository;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SurveyQuestionControllerTests {
    @MockBean
    private SurveyQuestionRepository surveyQuestionRepository;

    @MockBean
    private SurveyRepository surveyRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    public void getSurveyQuestion_Success() throws Exception {
        // Mock the behavior of SurveyRepository
        when(surveyQuestionRepository.findAll()).thenReturn(List.of(new SurveyQuestion( "A vi kako te?")));

        // Perform GET request to fetch all surveys
        mockMvc.perform(MockMvcRequestBuilders.get("/surveyquestions"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sadrzaj", Matchers.is("A vi kako te?")));
    }

    @Test
    public void createSurveyQuestion_ValidInput_Success() throws Exception {
        // Mock Survey object
        Survey survey = new Survey(1L, "user_uid", "Survey Title", "Survey Description", 1);

        // Mock SurveyQuestionDTO object
        SurveyQuestionDTO surveyQuestionDTO = new SurveyQuestionDTO();
        surveyQuestionDTO.setSurveyId(1L);
        surveyQuestionDTO.setSadrzaj("Sample Survey Question");

        // Mock the behavior of SurveyRepository to return a mock Survey object
        when(surveyRepository.findById(1)).thenReturn(Optional.of(survey));

        // Perform POST request to create a new survey question
        mockMvc.perform(post("/surveyquestions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(surveyQuestionDTO)))
                .andExpect(status().isCreated());

        // Verify that surveyQuestionRepository.save() was called with a SurveyQuestion object
        verify(surveyQuestionRepository, times(1)).save(any(SurveyQuestion.class));
    }

    @Test
    public void createSurveyQuestion_InvalidInput_Fail() throws Exception {
        // Create a SurveyQuestionDTO with invalid data (e.g., missing required fields)
        SurveyQuestionDTO surveyQuestionDTO = new SurveyQuestionDTO();

        // Perform POST request to create a new survey question
        mockMvc.perform(post("/surveyquestions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(surveyQuestionDTO)))
                .andExpect(status().isForbidden());

        // Verify that surveyQuestionRepository.save() was not called
        verify(surveyQuestionRepository, never()).save(any(SurveyQuestion.class));
    }

    @Test
    public void createSurveyQuestion_SurveyNotFound_Fail() throws Exception {
        // Mock SurveyQuestionDTO object
        SurveyQuestionDTO surveyQuestionDTO = new SurveyQuestionDTO();
        surveyQuestionDTO.setSurveyId(1L); // Assuming there is no survey with ID 1 in the database
        surveyQuestionDTO.setSadrzaj("Sample Survey Question");

        // Mock the behavior of SurveyRepository to return an empty optional, indicating survey not found
        when(surveyRepository.findById(1)).thenReturn(Optional.empty());

        // Perform POST request to create a new survey question
        mockMvc.perform(post("/surveyquestions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(surveyQuestionDTO)))
                .andExpect(status().isForbidden());

        // Verify that surveyQuestionRepository.save() was not called
        verify(surveyQuestionRepository, never()).save(any(SurveyQuestion.class));
    }
    @Test
    public void createSurveyQuestion_ValidationError_Fail() throws Exception {
        // Mock Survey object
        Survey survey = new Survey(1L, "user_uid", "Survey Title", "Survey Description", 1);

        // Mock SurveyQuestionDTO object with invalid data (e.g., null survey ID)
        SurveyQuestionDTO surveyQuestionDTO = new SurveyQuestionDTO();
        surveyQuestionDTO.setSurveyId(null); // Invalid: survey ID is null
        surveyQuestionDTO.setSadrzaj("Sample Survey Question");

        // Mock the behavior of SurveyRepository to return a mock Survey object
        when(surveyRepository.findById(1)).thenReturn(Optional.of(survey));

        // Perform POST request to create a new survey question
        mockMvc.perform(post("/surveyquestions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(surveyQuestionDTO)))
                .andExpect(status().isForbidden());

        // Verify that surveyQuestionRepository.save() was not called
        verify(surveyQuestionRepository, never()).save(any(SurveyQuestion.class));
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
