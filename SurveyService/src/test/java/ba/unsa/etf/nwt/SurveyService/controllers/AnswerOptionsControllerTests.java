package ba.unsa.etf.nwt.SurveyService.controllers;

import ba.unsa.etf.nwt.SurveyService.DTO.AnswerOptionsDTO;
import ba.unsa.etf.nwt.SurveyService.model.AnswerOptions;
import ba.unsa.etf.nwt.SurveyService.model.SurveyQuestion;
import ba.unsa.etf.nwt.SurveyService.repositories.AnswerOptionsRepository;
import ba.unsa.etf.nwt.SurveyService.repositories.SurveyQuestionRepository;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Optional;

import static ba.unsa.etf.nwt.SurveyService.controllers.SurveyControllerTests.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AnswerOptionsControllerTests {
    @MockBean
    private AnswerOptionsRepository answerOptionsRepository;
    @MockBean
    private SurveyQuestionRepository surveyQuestionRepository;
    @Mock
    private Validator validator;
    @Autowired
    private MockMvc mockMvc;
    @Test
    public void getAnswerOptions_Success() throws Exception {
        // Mock the behavior of AnswerOptionsRepository
        when(answerOptionsRepository.findAll()).thenReturn(List.of(new AnswerOptions("Sample Answer")));

        // Perform GET request to fetch all answer options
        mockMvc.perform(MockMvcRequestBuilders.get("/answeroptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].sadrzaj", Matchers.is("Sample Answer")));
    }

    @Test
    public void createAnswerOptions_Success() throws Exception {
        // Mock SurveyQuestion object
        SurveyQuestion surveyQuestion = new SurveyQuestion("Sample Question");

        // Mock AnswerOptionsDTO object
        AnswerOptionsDTO answerOptionsDTO = new AnswerOptionsDTO();
        answerOptionsDTO.setQuestionId(1L);
        answerOptionsDTO.setSadrzaj("Sample Answer");

        // Mock the behavior of SurveyQuestionRepository to return a mock SurveyQuestion object
        when(surveyQuestionRepository.findById(1L)).thenReturn(Optional.of(surveyQuestion));

        // Perform POST request to create new answer options
        mockMvc.perform(MockMvcRequestBuilders.post("/answeroptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(answerOptionsDTO)))
                .andExpect(status().isCreated());

        // Verify that answerOptionsRepository.save() was called with a AnswerOptions object
        verify(answerOptionsRepository, times(1)).save(any(AnswerOptions.class));
    }

    @Test
    public void createAnswerOptions_SurveyQuestionNotFound_Fail() throws Exception {
        // Mock AnswerOptionsDTO object
        AnswerOptionsDTO answerOptionsDTO = new AnswerOptionsDTO();
        answerOptionsDTO.setQuestionId(1L); // Assuming there is no survey question with ID 1 in the database
        answerOptionsDTO.setSadrzaj("Sample Answer");

        // Mock the behavior of SurveyQuestionRepository to return an empty optional, indicating survey question not found
        when(surveyQuestionRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform POST request to create new answer options
        mockMvc.perform(MockMvcRequestBuilders.post("/answeroptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(answerOptionsDTO)))
                .andExpect(status().isForbidden());

        // Verify that answerOptionsRepository.save() was not called
        verify(answerOptionsRepository, never()).save(any(AnswerOptions.class));
    }

    @Test
    public void createAnswerOptions_ValidationError_Fail() throws Exception {
        // Mock AnswerOptionsDTO object with invalid data (e.g., missing required fields)
        AnswerOptionsDTO answerOptionsDTO = new AnswerOptionsDTO();

        // Perform POST request to create new answer options
        mockMvc.perform(MockMvcRequestBuilders.post("/answeroptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(answerOptionsDTO)))
                .andExpect(status().isForbidden());

        // Verify that answerOptionsRepository.save() was not called
        verify(answerOptionsRepository, never()).save(any(AnswerOptions.class));
    }

    @Test
    public void getAnswerOptionsByID_Success() throws Exception {
        // Mock the behavior of AnswerOptionsRepository
        AnswerOptions answerOptions = new AnswerOptions(1L, "Sample Answer");
        when(answerOptionsRepository.findById(1L)).thenReturn(Optional.of(answerOptions));

        // Perform GET request to fetch answer options by ID
        mockMvc.perform(MockMvcRequestBuilders.get("/answeroptions/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sadrzaj", Matchers.is("Sample Answer")));
    }

    @Test
    public void getAnswerOptionsByID_NotFound() throws Exception {
        // Mock the behavior of AnswerOptionsRepository to return an empty optional, indicating answer options not found
        when(answerOptionsRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform GET request with non-existing answer option ID
        mockMvc.perform(MockMvcRequestBuilders.get("/answeroptions/id/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", Matchers.is("validation")));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Nije pronadjen ponudjeni odgovor na pitanju sa tim ID-em.")));
    }

    @Test
    public void getAnswerOptionsByQuestionID_Success() throws Exception {
        // Mock the behavior of SurveyQuestionRepository
        SurveyQuestion surveyQuestion = new SurveyQuestion("Sample Question");
        when(surveyQuestionRepository.findById(1L)).thenReturn(Optional.of(surveyQuestion));

        // Mock the behavior of AnswerOptionsRepository
        AnswerOptions answerOptions = new AnswerOptions("Sample Answer");
        when(answerOptionsRepository.findByAnketaPitanje(surveyQuestion)).thenReturn(List.of(answerOptions));

        // Perform GET request to fetch answer options by question ID
        mockMvc.perform(MockMvcRequestBuilders.get("/answeroptions/question-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].sadrzaj", Matchers.is("Sample Answer")));
    }

    @Test
    public void getAnswerOptionsByQuestionID_QuestionNotFound() throws Exception {
        // Mock the behavior of SurveyQuestionRepository to return an empty optional,
        // indicating that the question is not found
        when(surveyQuestionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Perform GET request to fetch answer options by question ID
        mockMvc.perform(MockMvcRequestBuilders.get("/answeroptions/question-id/{questionId}", 123L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteAnswerOptions_Success() throws Exception {
        // Mock the behavior of AnswerOptionsRepository
        AnswerOptions answerOptions = new AnswerOptions(1L, "Sample Answer");
        when(answerOptionsRepository.findById(1L)).thenReturn(Optional.of(answerOptions));

        // Perform DELETE request to delete answer options by ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/answeroptions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sadrzaj", Matchers.is("Sample Answer")));
    }

    @Test
    public void deleteAnswerOptions_NotFound() throws Exception {
        // Mock the behavior of AnswerOptionsRepository to return an empty optional, indicating answer options not found
        when(answerOptionsRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform DELETE request with non-existing answer option ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/answeroptions/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", Matchers.is("validation")));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Nije pronadjen ponudjeni odgovor na pitanju sa tim ID-em.")));
    }

    @Test
    public void updateAnswerOptions_ValidInput_Success() throws Exception {
        // Mock the behavior of AnswerOptionsRepository
        AnswerOptions existingAnswerOptions = new AnswerOptions(1L, "Old Answer");
        when(answerOptionsRepository.findById(1L)).thenReturn(Optional.of(existingAnswerOptions));

        // Mock the behavior of SurveyQuestionRepository
        SurveyQuestion surveyQuestion = new SurveyQuestion("Sample Question");
        when(surveyQuestionRepository.findById(1L)).thenReturn(Optional.of(surveyQuestion));

        // Mock AnswerOptionsDTO object
        AnswerOptionsDTO answerOptionsDTO = new AnswerOptionsDTO();
        answerOptionsDTO.setQuestionId(1L);
        answerOptionsDTO.setSadrzaj("New Answer");

        // Perform PUT request to update answer options
        mockMvc.perform(MockMvcRequestBuilders.put("/answeroptions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(answerOptionsDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sadrzaj").value("New Answer"));
    }


    @Test
    public void updateAnswerOptions_InvalidSurveyQuestion_Fail() throws Exception {
        // Mock existing AnswerOptions object
        AnswerOptions existingAnswerOptions = new AnswerOptions(1L, "Old Answer");

        // Mock the behavior of AnswerOptionsRepository to return the existing AnswerOptions object
        when(answerOptionsRepository.findById(1L)).thenReturn(Optional.of(existingAnswerOptions));

        // Mock AnswerOptionsDTO object with updated data
        AnswerOptionsDTO answerOptionsDTO = new AnswerOptionsDTO();
        answerOptionsDTO.setQuestionId(1L); // Assuming there is no survey question with ID 1 in the database
        answerOptionsDTO.setSadrzaj("New Answer");

        // Mock the behavior of SurveyQuestionRepository to return an empty optional, indicating survey question not found
        when(surveyQuestionRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform PUT request to update answer options
        mockMvc.perform(MockMvcRequestBuilders.put("/answeroptions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(answerOptionsDTO)))
                .andExpect(status().isForbidden());

        // Verify that answerOptionsRepository.save() was not called
        verify(answerOptionsRepository, never()).save(existingAnswerOptions);
    }

    @Test
    public void updateAnswerOptions_ValidationError_Fail() throws Exception {
        // Mock existing AnswerOptions object
        AnswerOptions existingAnswerOptions = new AnswerOptions(1L, "Old Answer");

        // Mock the behavior of AnswerOptionsRepository to return the existing AnswerOptions object
        when(answerOptionsRepository.findById(1L)).thenReturn(Optional.of(existingAnswerOptions));

        // Mock AnswerOptionsDTO object with invalid data (e.g., missing required fields)
        AnswerOptionsDTO answerOptionsDTO = new AnswerOptionsDTO();

        // Perform PUT request to update answer options
        mockMvc.perform(MockMvcRequestBuilders.put("/answeroptions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(answerOptionsDTO)))
                .andExpect(status().isForbidden());

        // Verify that answerOptionsRepository.save() was not called
        verify(answerOptionsRepository, never()).save(existingAnswerOptions);
    }

    @Test
    public void updateSadrzaj_Success() throws Exception {
        // Mock the behavior of AnswerOptionsRepository
        AnswerOptions answerOptions = new AnswerOptions(1L, "Old Answer");
        when(answerOptionsRepository.findById(1L)).thenReturn(Optional.of(answerOptions));

        // Perform PUT request to update answer options
        mockMvc.perform(MockMvcRequestBuilders.patch("/answeroptions/1/sadrzaj/New Answer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sadrzaj", Matchers.is("New Answer")));
    }

    @Test
    public void updateSadrzaj_NotFound() throws Exception {
        // Mock the behavior of AnswerOptionsRepository to return an empty optional, indicating answer options not found
        when(answerOptionsRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform PUT request with non-existing answer option ID
        mockMvc.perform(MockMvcRequestBuilders.patch("/answeroptions/1/sadrzaj/New Answer"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", Matchers.is("validation")));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Neispravni parametri!")));
    }
}
