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
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

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
        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());

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
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        // Perform POST request to create a new survey question
        mockMvc.perform(post("/surveyquestions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(surveyQuestionDTO)))
                .andExpect(status().isForbidden());

        // Verify that surveyQuestionRepository.save() was not called
        verify(surveyQuestionRepository, never()).save(any(SurveyQuestion.class));
    }

    @Test
    public void getSurveyQuestionByID_Success() throws Exception {
        // Mock the behavior of SurveyQuestionRepository
        SurveyQuestion surveyQuestion = new SurveyQuestion("A vi kako te?");
        when(surveyQuestionRepository.findById(1L)).thenReturn(Optional.of(surveyQuestion));

        // Perform GET request to fetch survey question by ID
        mockMvc.perform(MockMvcRequestBuilders.get("/surveyquestions/id/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sadrzaj", Matchers.is("A vi kako te?")));
    }

    @Test
    public void getSurveyQuestionByID_NotFound() throws Exception {
        // Mock the behavior of SurveyQuestionRepository to return an empty optional, indicating survey question not found
        when(surveyQuestionRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform GET request with non-existing survey question ID
        mockMvc.perform(MockMvcRequestBuilders.get("/surveyquestions/id/1"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Nije pronadjeno pitanje na anketi sa tim ID-em.")));
    }

    @Test
    public void getSurveyQuestionBySurveyID_Success() throws Exception {
        // Mock the behavior of SurveyRepository
        Survey survey = new Survey(1L, "user_uid", "Survey Title", "Survey Description", 1);
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        // Mock the behavior of SurveyQuestionRepository
        SurveyQuestion surveyQuestion1 = new SurveyQuestion("Question 1");
        SurveyQuestion surveyQuestion2 = new SurveyQuestion("Question 2");
        when(surveyQuestionRepository.findByAnketa(survey)).thenReturn(List.of(surveyQuestion1, surveyQuestion2));

        // Perform GET request to fetch survey questions by survey ID
        mockMvc.perform(MockMvcRequestBuilders.get("/surveyquestions/survey-id/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sadrzaj", Matchers.is("Question 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sadrzaj", Matchers.is("Question 2")));
    }

    @Test
    public void getSurveyQuestionBySurveyID_SurveyNotFound() throws Exception {
        // Mock the behavior of SurveyRepository to return an empty optional, indicating survey not found
        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform GET request with non-existing survey ID
        mockMvc.perform(MockMvcRequestBuilders.get("/surveyquestions/survey-id/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteSurveyQuestion_Success() throws Exception {
        // Mock the behavior of SurveyQuestionRepository
        SurveyQuestion surveyQuestion = new SurveyQuestion("Sample Question");
        when(surveyQuestionRepository.findById(1L)).thenReturn(Optional.of(surveyQuestion));

        // Perform DELETE request to delete survey question by ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/surveyquestions/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteSurveyQuestion_NotFound() throws Exception {
        // Mock the behavior of SurveyQuestionRepository to return an empty optional, indicating survey question not found
        when(surveyQuestionRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform DELETE request with non-existing survey question ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/surveyquestions/1"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Nije pronadjeno pitanje na anketi sa tim ID-em.")));
    }

//    @Test
//    public void updateSurveyQuestion_ValidInput_Success() throws Exception {
//        // Mock existing SurveyQuestion object
//        SurveyQuestion existingSurveyQuestion = new SurveyQuestion("Existing Survey Question");
//        existingSurveyQuestion.setID(1L);
//
//        // Mock the behavior of SurveyQuestionRepository to return the existing SurveyQuestion object
//        when(surveyQuestionRepository.findById(1L)).thenReturn(Optional.of(existingSurveyQuestion));
//
//        // Mock the behavior of SurveyRepository to return an existing survey
//        when(surveyRepository.findById(1L)).thenReturn(Optional.of(new Survey()));
//
//        // Mock SurveyQuestionDTO object with updated data
//        SurveyQuestionDTO updatedSurveyQuestionDTO = new SurveyQuestionDTO();
//        updatedSurveyQuestionDTO.setSurveyId(1L); // Assuming existing survey ID
//        updatedSurveyQuestionDTO.setSadrzaj("Updated Survey Question");
//
//        // Perform PUT request to update the survey question
//        mockMvc.perform(MockMvcRequestBuilders.put("/surveyquestions/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(updatedSurveyQuestionDTO)))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.sadrzaj", Matchers.is("Updated Survey Question")));
//
//        // Verify that surveyQuestionRepository.save() was called with the updated SurveyQuestion object
//        verify(surveyQuestionRepository, times(1)).save(any(SurveyQuestion.class));
//    }

    @Test
    public void updateSurveyQuestion_ValidInput_Success() throws Exception {
        Survey survey = new Survey(1L, "user123", "Naslov ankete", "Opis ankete", 1);
        SurveyQuestion existingSurveyQuestion = new SurveyQuestion(1L, survey, "Existing Survey Question");

        // Mock the behavior of SurveyQuestionRepository to return the existing SurveyQuestion object
        when(surveyQuestionRepository.findById(1L)).thenReturn(Optional.of(existingSurveyQuestion));

        // Mock SurveyQuestionDTO object
        SurveyQuestionDTO surveyQuestionDTO = new SurveyQuestionDTO();
        surveyQuestionDTO.setSurveyId(1L);
        surveyQuestionDTO.setSadrzaj("Sample Survey Question");

        // Perform PUT request to update the survey question
        mockMvc.perform(MockMvcRequestBuilders.put("/surveyquestions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(surveyQuestionDTO)))
                .andExpect(status().isOk())
                // Assert that the response contains the updated 'sadrzaj' field
                .andExpect(MockMvcResultMatchers.jsonPath("$.sadrzaj").value("Sample Survey Question"));

        // Verify that surveyQuestionRepository.save() was called with the correct SurveyQuestion object
        verify(surveyQuestionRepository, times(1)).save(any(SurveyQuestion.class));
    }

    @Test
    public void updateSurveyQuestion_InvalidInput_Fail() throws Exception {
        // Mock existing SurveyQuestion object
        SurveyQuestion existingSurveyQuestion = new SurveyQuestion("Existing Survey Question");
        existingSurveyQuestion.setID(1L);

        // Mock the behavior of SurveyQuestionRepository to return the existing SurveyQuestion object
        when(surveyQuestionRepository.findById(1L)).thenReturn(Optional.of(existingSurveyQuestion));

        // Mock SurveyQuestionDTO object with invalid data
        SurveyQuestionDTO updatedSurveyQuestionDTO = new SurveyQuestionDTO();

        // Perform PUT request to update the survey question with invalid input
        mockMvc.perform(MockMvcRequestBuilders.put("/surveyquestions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedSurveyQuestionDTO)))
                .andExpect(status().isForbidden());

        // Verify that surveyQuestionRepository.save() was not called
        verify(surveyQuestionRepository, never()).save(any(SurveyQuestion.class));
    }

    @Test
    public void updateSurveyQuestion_SurveyNotFound_Fail() throws Exception {
        // Mock existing SurveyQuestion object
        SurveyQuestion existingSurveyQuestion = new SurveyQuestion("Existing Survey Question");
        existingSurveyQuestion.setID(1L);

        // Mock the behavior of SurveyQuestionRepository to return the existing SurveyQuestion object
        when(surveyQuestionRepository.findById(1L)).thenReturn(Optional.of(existingSurveyQuestion));

        // Mock the behavior of SurveyRepository to return an empty optional, indicating survey not found
        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());

        // Mock SurveyQuestionDTO object with updated data
        SurveyQuestionDTO updatedSurveyQuestionDTO = new SurveyQuestionDTO();
        updatedSurveyQuestionDTO.setSadrzaj("nesto");
        updatedSurveyQuestionDTO.setSurveyId(1L); // Assuming existing survey ID

        // Perform PUT request to update the survey question with non-existing survey ID
        mockMvc.perform(MockMvcRequestBuilders.put("/surveyquestions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedSurveyQuestionDTO)))
                .andExpect(status().isNotFound());

        // Verify that surveyQuestionRepository.save() was not called
        verify(surveyQuestionRepository, never()).save(any(SurveyQuestion.class));
    }

    @Test
    public void updateSurveyQuestion_NotFound_Fail() throws Exception {
        // Mock the behavior of SurveyQuestionRepository to return an empty optional, indicating survey question not found
        when(surveyQuestionRepository.findById(1L)).thenReturn(Optional.empty());

        // Mock SurveyQuestionDTO object with updated data
        SurveyQuestionDTO updatedSurveyQuestionDTO = new SurveyQuestionDTO();
        updatedSurveyQuestionDTO.setSurveyId(1L); // Assuming existing survey ID

        // Perform PUT request to update the non-existing survey question
        mockMvc.perform(MockMvcRequestBuilders.put("/surveyquestions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedSurveyQuestionDTO)))
                .andExpect(status().isNotFound());

        // Verify that surveyQuestionRepository.save() was not called
        verify(surveyQuestionRepository, never()).save(any(SurveyQuestion.class));
    }


    @Test
    public void updateSadrzaj_Success() throws Exception {
        // Mock the behavior of SurveyQuestionRepository
        SurveyQuestion surveyQuestion = new SurveyQuestion("Sample Question");
        when(surveyQuestionRepository.findById(1L)).thenReturn(Optional.of(surveyQuestion));

        // Perform PUT request to update content of survey question
        mockMvc.perform(MockMvcRequestBuilders.patch("/surveyquestions/1/sadrzaj/New Content")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sadrzaj", Matchers.is("New Content")));

        // Verify that surveyQuestionRepository.save() was called with the correct SurveyQuestion object
        verify(surveyQuestionRepository, times(1)).save(any(SurveyQuestion.class));
    }

    @Test
    public void updateSadrzaj_NotFound() throws Exception {
        // Mock the behavior of SurveyQuestionRepository to return an empty optional, indicating survey question not found
        when(surveyQuestionRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform PUT request with non-existing survey question ID
        mockMvc.perform(MockMvcRequestBuilders.patch("/surveyquestions/1/sadrzaj/New Content")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Neispravni parametri!")));
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
