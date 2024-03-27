package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.DTO.SurveyDTO;
import ba.unsa.etf.nwt.SurveyService.model.Survey;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SurveyControllerTests {
    @MockBean
    private SurveyRepository surveyRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    public void getSurveys_Success() throws Exception {
        // Mock the behavior of SurveyRepository
        when(surveyRepository.findAll()).thenReturn(List.of(new Survey("user123", "Naslov ankete", "Opis ankete", 1)));

        // Perform GET request to fetch all surveys
        mockMvc.perform(MockMvcRequestBuilders.get("/surveys"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].naslov", Matchers.is("Naslov ankete")));
    }

    @Test
    public void createSurvey_Success() throws Exception {
        // Mock the behavior of SurveyRepository
        when(surveyRepository.save(any(Survey.class))).thenReturn(new Survey(1L, "user123", "Naslov ankete", "Opis ankete", 1));

        // Create a survey DTO for the request body
        SurveyDTO surveyDTO = new SurveyDTO();
        surveyDTO.setUserUid("user123");
        surveyDTO.setNaslov("Naslov ankete");
        surveyDTO.setOpis("Opis ankete");
        surveyDTO.setStatus(1);

        // Perform POST request to create a new survey
        mockMvc.perform(post("/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(surveyDTO)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.naslov", Matchers.is("Naslov ankete")));

        // Verify that surveyRepository.save() was called with the correct Survey object
        verify(surveyRepository, times(1)).save(any(Survey.class));
    }

    // Test for failure scenario when creating a survey with invalid input data
    @Test
    public void createSurvey_InvalidInput_Fail() throws Exception {
        // Create a survey DTO with invalid data (e.g., missing required fields)
        SurveyDTO surveyDTO = new SurveyDTO();

        // Perform POST request to create a new survey
        mockMvc.perform(post("/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(surveyDTO)))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));

        // Verify that surveyRepository.save() was not called
        verify(surveyRepository, never()).save(any(Survey.class));
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
