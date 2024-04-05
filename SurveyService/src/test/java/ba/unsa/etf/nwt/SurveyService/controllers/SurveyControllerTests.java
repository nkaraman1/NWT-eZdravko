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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public void getSurveys_EmptyList_Success() throws Exception {
        // Mock the behavior of SurveyRepository
        when(surveyRepository.findAll()).thenReturn(Collections.emptyList());

        // Perform GET request when no surveys are found
        mockMvc.perform(MockMvcRequestBuilders.get("/surveys"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(0)));
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

    @Test
    public void getSurveyByID_Success() throws Exception {
        // Mock the behavior of SurveyRepository
        Survey survey = new Survey(1L, "user123", "Naslov ankete", "Opis ankete", 1);
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        // Perform GET request to fetch survey by ID
        mockMvc.perform(MockMvcRequestBuilders.get("/surveys/id/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.naslov", Matchers.is("Naslov ankete")));
    }

    @Test
    public void getSurveyByID_NotFound() throws Exception {
        // Mock the behavior of SurveyRepository
        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform GET request with non-existing survey ID
        mockMvc.perform(MockMvcRequestBuilders.get("/surveys/id/1"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Nije pronadjena anketa sa tim ID-em.")));
    }

    @Test
    public void getSurveyByNaslov_Success() throws Exception {
        // Mock the behavior of SurveyRepository
        Survey survey = new Survey("user123", "Naslov ankete", "Opis ankete", 1);
        when(surveyRepository.findByNaslov("Naslov ankete")).thenReturn(Optional.of(survey));

        // Perform GET request to fetch survey by title
        mockMvc.perform(MockMvcRequestBuilders.get("/surveys/naslov/Naslov ankete"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.naslov", Matchers.is("Naslov ankete")));
    }

    @Test
    public void getSurveyByNaslov_NotFound() throws Exception {
        // Mock the behavior of SurveyRepository
        when(surveyRepository.findByNaslov("Naslov ankete")).thenReturn(Optional.empty());

        // Perform GET request with non-existing survey title
        mockMvc.perform(MockMvcRequestBuilders.get("/surveys/naslov/Naslov ankete"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Nije pronadjena anketa sa tim naslovom.")));
    }

    @Test
    public void getSurveyByStatus_Success() throws Exception {
        // Mock the behavior of SurveyRepository
        List<Survey> surveys = List.of(new Survey("user123", "Naslov ankete", "Opis ankete", 1));
        when(surveyRepository.findByStatus(1)).thenReturn(surveys);

        // Perform GET request to fetch surveys by status
        mockMvc.perform(MockMvcRequestBuilders.get("/surveys/status/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].naslov", Matchers.is("Naslov ankete")));
    }

    @Test
    public void getSurveyByStatus_NoSurveysFound() throws Exception {
        // Mock the behavior of SurveyRepository
        when(surveyRepository.findByStatus(1)).thenReturn(Collections.emptyList());

        // Perform GET request with non-existing survey status
        mockMvc.perform(MockMvcRequestBuilders.get("/surveys/status/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(0)));
    }

    @Test
    public void deleteSurvey_Success() throws Exception {
        // Mock the behavior of SurveyRepository
        Survey survey = new Survey(1L, "user123", "Naslov ankete", "Opis ankete", 1);
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        // Perform DELETE request to delete survey by ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/surveys/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.naslov", Matchers.is("Naslov ankete")));

        // Verify that surveyRepository.deleteById() was called with the correct ID
        verify(surveyRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteSurvey_NotFound() throws Exception {
        // Mock the behavior of SurveyRepository
        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform DELETE request with non-existing survey ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/surveys/1"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Nije pronadjena anketa sa tim ID-em.")));

        // Verify that surveyRepository.deleteById() was not called
        verify(surveyRepository, never()).deleteById(anyLong());
    }

    @Test
    public void updateSurvey_Success() throws Exception {
        // Mock the behavior of SurveyRepository
        Survey existingSurvey = new Survey(1L, "user123", "Naslov ankete", "Opis ankete", 1);
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(existingSurvey));
        when(surveyRepository.save(any(Survey.class))).thenReturn(existingSurvey); // mock save operation

        SurveyDTO updatedSurveyDTO = new SurveyDTO();
        updatedSurveyDTO.setUserUid("updatedUser123");
        updatedSurveyDTO.setNaslov("Updated Naslov ankete");
        updatedSurveyDTO.setOpis("Updated Opis ankete");
        updatedSurveyDTO.setStatus(1);

        mockMvc.perform(MockMvcRequestBuilders.put("/surveys/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedSurveyDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user_uid").value("updatedUser123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.naslov").value("Updated Naslov ankete"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.opis").value("Updated Opis ankete"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(1));
    }

    @Test
    public void updateSurvey_SurveyNotFound() throws Exception {
        // Mock the behavior of SurveyRepository
        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());

        // Create a SurveyDTO with updated values
        SurveyDTO updatedSurveyDTO = new SurveyDTO();
        updatedSurveyDTO.setUserUid("updatedUser123");
        updatedSurveyDTO.setNaslov("Updated Naslov ankete");
        updatedSurveyDTO.setOpis("Updated Opis ankete");
        updatedSurveyDTO.setStatus(1);

        // Perform PUT request to update the survey
        mockMvc.perform(MockMvcRequestBuilders.put("/surveys/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedSurveyDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateSurvey_InvalidInput_Fail() throws Exception {
        Survey existingSurvey = new Survey(1L, "user123", "Naslov ankete", "Opis ankete", 1);
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(existingSurvey));
        when(surveyRepository.save(any(Survey.class))).thenReturn(existingSurvey);

        // Create a SurveyDTO with invalid data (e.g., missing required fields)
        SurveyDTO updatedSurveyDTO = new SurveyDTO();

        // Perform PUT request to update the survey with invalid data
        mockMvc.perform(MockMvcRequestBuilders.put("/surveys/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedSurveyDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateSurvey_InvalidStatus_Fail() throws Exception {
        // Mock the behavior of SurveyRepository
        Survey existingSurvey = new Survey(1L, "user123", "Naslov ankete", "Opis ankete", 1);
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(existingSurvey));
        when(surveyRepository.save(any(Survey.class))).thenReturn(existingSurvey); // mock save operation

        // Create a SurveyDTO with invalid data (empty strings and negative status)
        SurveyDTO updatedSurveyDTO = new SurveyDTO();
        updatedSurveyDTO.setUserUid("user123");
        updatedSurveyDTO.setNaslov("Naslov");
        updatedSurveyDTO.setOpis("Opis");
        updatedSurveyDTO.setStatus(2); // Invalid status

        // Perform PUT request to update the survey with invalid data
        mockMvc.perform(MockMvcRequestBuilders.put("/surveys/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedSurveyDTO)))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
    }

    @Test
    public void updateSurvey_Failure() throws Exception {
        // Mock the behavior of SurveyRepository
        Survey existingSurvey = new Survey(1L, "user123", "Naslov ankete", "Opis ankete", 1);
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(existingSurvey));
        when(surveyRepository.save(any(Survey.class))).thenThrow(new RuntimeException("Failed to save survey")); // mock save failure

        // Create a SurveyDTO with updated values
        SurveyDTO updatedSurveyDTO = new SurveyDTO();
        updatedSurveyDTO.setUserUid("updatedUser123");
        updatedSurveyDTO.setNaslov("Updated Naslov ankete");
        updatedSurveyDTO.setOpis("Updated Opis ankete");
        updatedSurveyDTO.setStatus(1);

        mockMvc.perform(MockMvcRequestBuilders.put("/surveys/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedSurveyDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Failed to save survey"));
    }

    @Test
    public void updateUserUid_Success() throws Exception {
        // Mock the behavior of SurveyRepository
        Survey survey = new Survey(1L, "user123", "Naslov ankete", "Opis ankete", 1);
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        // Perform PUT request to update user UID of survey
        mockMvc.perform(MockMvcRequestBuilders.patch("/surveys/1/user-uid/newUser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user_uid", Matchers.is("newUser")));

        // Verify that surveyRepository.save() was called with the correct Survey object
        verify(surveyRepository, times(1)).save(any(Survey.class));
    }

    @Test
    public void updateUserUid_NotFound() throws Exception {
        // Mock the behavior of SurveyRepository
        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform PUT request with non-existing survey ID
        mockMvc.perform(MockMvcRequestBuilders.patch("/surveys/1/user-uid/newUser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Neispravni parametri!")));

        // Verify that surveyRepository.save() was not called
        verify(surveyRepository, never()).save(any(Survey.class));
    }

    @Test
    public void updateNaslov_Success() throws Exception {
        // Mock the behavior of SurveyRepository
        Survey survey = new Survey(1L, "user123", "Naslov ankete", "Opis ankete", 1);
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        // Perform PUT request to update title of survey
        mockMvc.perform(MockMvcRequestBuilders.patch("/surveys/1/naslov/New Title")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.naslov", Matchers.is("New Title")));

        // Verify that surveyRepository.save() was called with the correct Survey object
        verify(surveyRepository, times(1)).save(any(Survey.class));
    }

    @Test
    public void updateNaslov_NotFound() throws Exception {
        // Mock the behavior of SurveyRepository
        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform PUT request with non-existing survey ID
        mockMvc.perform(MockMvcRequestBuilders.patch("/surveys/1/naslov/New Title")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Neispravni parametri!")));

        // Verify that surveyRepository.save() was not called
        verify(surveyRepository, never()).save(any(Survey.class));
    }


    @Test
    public void updateOpis_Success() throws Exception {
        // Mock the behavior of SurveyRepository
        Survey survey = new Survey(1L, "user123", "Naslov ankete", "Opis ankete", 1);
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        // Perform PUT request to update description of survey
        mockMvc.perform(MockMvcRequestBuilders.patch("/surveys/1/opis/New Description")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.opis", Matchers.is("New Description")));

        // Verify that surveyRepository.save() was called with the correct Survey object
        verify(surveyRepository, times(1)).save(any(Survey.class));
    }

    @Test
    public void updateOpis_NotFound() throws Exception {
        // Mock the behavior of SurveyRepository
        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform PUT request with non-existing survey ID
        mockMvc.perform(MockMvcRequestBuilders.patch("/surveys/1/opis/New Description")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Neispravni parametri!")));

        // Verify that surveyRepository.save() was not called
        verify(surveyRepository, never()).save(any(Survey.class));
    }


    @Test
    public void updateStatus_Success() throws Exception {
        // Mock the behavior of SurveyRepository
        Survey survey = new Survey(1L, "user123", "Naslov ankete", "Opis ankete", 1);
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        // Perform PUT request to update status of survey
        mockMvc.perform(MockMvcRequestBuilders.patch("/surveys/1/status/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(2)));

        // Verify that surveyRepository.save() was called with the correct Survey object
        verify(surveyRepository, times(1)).save(any(Survey.class));
    }

    @Test
    public void updateStatus_NotFound() throws Exception {
        // Mock the behavior of SurveyRepository
        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform PUT request with non-existing survey ID
        mockMvc.perform(MockMvcRequestBuilders.patch("/surveys/1/status/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Neispravni parametri!")));

        // Verify that surveyRepository.save() was not called
        verify(surveyRepository, never()).save(any(Survey.class));
    }

    // Utility method to convert objects to JSON string
    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
