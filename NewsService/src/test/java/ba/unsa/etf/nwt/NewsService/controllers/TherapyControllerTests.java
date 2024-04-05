package ba.unsa.etf.nwt.NewsService.controllers;

import ba.unsa.etf.nwt.NewsService.DTO.TherapyDTO;
import ba.unsa.etf.nwt.NewsService.model.Therapy;
import ba.unsa.etf.nwt.NewsService.repositories.TherapyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TherapyControllerTests {

    @MockBean
    private TherapyRepository therapyRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    public void getTherapies_Success() throws Exception {
        // Mock the behavior of TherapyRepository
        when(therapyRepository.findAll()).thenReturn(List.of(new Therapy(1L, "Medicine", "Note", 5, "PatientUID", "DoctorUID")));

        // Perform GET request to fetch all therapies
        mockMvc.perform(MockMvcRequestBuilders.get("/therapy"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lijek", Matchers.is("Medicine")));
    }

    @Test
    public void getTherapies_EmptyList_Success() throws Exception {
        // Mock the behavior of TherapyRepository
        when(therapyRepository.findAll()).thenReturn(Collections.emptyList());

        // Perform GET request when no therapies are found
        mockMvc.perform(MockMvcRequestBuilders.get("/therapy"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(0)));
    }

    @Test
    public void createTherapy_Success() throws Exception {
        // Mock the behavior of TherapyRepository
        when(therapyRepository.save(any(Therapy.class))).thenReturn(new Therapy(1L, "Medicine", "Note", 5, "PatientUID", "DoctorUID"));

        // Create a therapy DTO for the request body
        TherapyDTO therapyDTO = new TherapyDTO("Medicine", "Note", 5, "PatientUID", "DoctorUID");

        // Perform POST request to create a new therapy
        mockMvc.perform(post("/therapy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(therapyDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lijek", Matchers.is("Medicine")));

        // Verify that therapyRepository.save() was called with the correct Therapy object
        verify(therapyRepository, times(1)).save(any(Therapy.class));
    }

    @Test
    public void createTherapy_InvalidInput_Fail() throws Exception {
        // Prepare invalid therapy data
        TherapyDTO therapyDTO = new TherapyDTO();

        // Perform the POST request
        MvcResult result = mockMvc.perform(post("/therapy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(therapyDTO)))
                .andExpect(status().isForbidden()) // Expecting 403
                .andReturn();

        // Extract the response body as a String
        String responseBody = result.getResponse().getContentAsString();

        // Assert the response body contains the expected error messages
        assertThat(responseBody).contains("Lijek je obavezan");
        assertThat(responseBody).contains("KoliÄ\u008Dina je obavezna");
        assertThat(responseBody).contains("UID pacijenta je obavezan");
        assertThat(responseBody).contains("UID doktora je obavezan");
    }

    @Test
    public void getTherapyByID_Success() throws Exception {
        // Mock the behavior of TherapyRepository
        Therapy therapy = new Therapy(1L, "Medicine", "Note", 5, "PatientUID", "DoctorUID");
        when(therapyRepository.findById(1L)).thenReturn(Optional.of(therapy));

        // Perform GET request to fetch therapy by ID
        mockMvc.perform(MockMvcRequestBuilders.get("/therapy/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lijek", Matchers.is("Medicine")));
    }

    @Test
    public void getTherapyByID_NotFound() throws Exception {
        // Mock the behavior of TherapyRepository
        when(therapyRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform GET request with non-existing therapy ID
        mockMvc.perform(MockMvcRequestBuilders.get("/therapy/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Nije pronadjena terapija sa tim ID-em.")));
    }

    @Test
    public void deleteTherapy_Success() throws Exception {
        // Mock the behavior of TherapyRepository
        Therapy therapy = new Therapy(1L, "Medicine", "Note", 5, "PatientUID", "DoctorUID");
        when(therapyRepository.findById(1L)).thenReturn(Optional.of(therapy));

        // Perform DELETE request to delete therapy by ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/therapy/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lijek", Matchers.is("Medicine")));

        // Verify that therapyRepository.deleteById() was called with the correct ID
        verify(therapyRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteTherapy_NotFound() throws Exception {
        // Mock the behavior of TherapyRepository
        when(therapyRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform DELETE request with non-existing therapy ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/therapy/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Nije pronadjena terapija sa tim ID-em.")));

        // Verify that therapyRepository.deleteById() was not called
        verify(therapyRepository, never()).deleteById(anyLong());
    }

    @Test
    public void updateTherapy_Success() throws Exception {
        // Mock the behavior of TherapyRepository
        Therapy existingTherapy = new Therapy(1L, "Medicine", "Note", 5, "PatientUID", "DoctorUID");
        when(therapyRepository.findById(1L)).thenReturn(Optional.of(existingTherapy));
        when(therapyRepository.save(any(Therapy.class))).thenReturn(existingTherapy); // mock save operation

        TherapyDTO updatedTherapyDTO = new TherapyDTO("Updated Medicine", "Updated Note", 10, "Updated PatientUID", "Updated DoctorUID");

        mockMvc.perform(MockMvcRequestBuilders.put("/therapy/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedTherapyDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lijek").value("Updated Medicine"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.napomena").value("Updated Note"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.kolicina").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pacijent_uid").value("Updated PatientUID"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.doktor_uid").value("Updated DoctorUID"));
    }

    @Test
    public void updateTherapy_NotFound() throws Exception {
        // Mock the behavior of TherapyRepository
        when(therapyRepository.findById(1L)).thenReturn(Optional.empty());

        // Create a TherapyDTO with updated values
        TherapyDTO updatedTherapyDTO = new TherapyDTO("Updated Medicine", "Updated Note", 10, "Updated PatientUID", "Updated DoctorUID");

        // Perform PUT request to update the therapy
        mockMvc.perform(MockMvcRequestBuilders.put("/therapy/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedTherapyDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateTherapy_InvalidInput_Fail() throws Exception {
        Therapy existingTherapy = new Therapy(1L, "Medicine", "Note", 5, "PatientUID", "DoctorUID");
        when(therapyRepository.findById(1L)).thenReturn(Optional.of(existingTherapy));
        when(therapyRepository.save(any(Therapy.class))).thenReturn(existingTherapy);

        // Create a TherapyDTO with invalid data (e.g., missing required fields)
        TherapyDTO updatedTherapyDTO = new TherapyDTO();

        // Perform PUT request to update the therapy with invalid data
        mockMvc.perform(MockMvcRequestBuilders.put("/therapy/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedTherapyDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void updateTherapy_Failure() throws Exception {
        // Mock the behavior of TherapyRepository
        Therapy existingTherapy = new Therapy(1L, "Medicine", "Note", 5, "PatientUID", "DoctorUID");
        when(therapyRepository.findById(1L)).thenReturn(Optional.of(existingTherapy));
        when(therapyRepository.save(any(Therapy.class))).thenThrow(new RuntimeException("Failed to save therapy")); // mock save failure

        // Create a TherapyDTO with updated values
        TherapyDTO updatedTherapyDTO = new TherapyDTO("Updated Medicine", "Updated Note", 10, "Updated PatientUID", "Updated DoctorUID");

        mockMvc.perform(MockMvcRequestBuilders.put("/therapy/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedTherapyDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Failed to save therapy"));
    }

    // Utility method to convert object to JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
