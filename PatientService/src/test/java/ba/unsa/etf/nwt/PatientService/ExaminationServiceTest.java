package ba.unsa.etf.nwt.PatientService;

import ba.unsa.etf.nwt.PatientService.model.Examination;
import ba.unsa.etf.nwt.PatientService.repositories.ExaminationRepository;
import ba.unsa.etf.nwt.PatientService.repositories.ReferralRepository;
import ba.unsa.etf.nwt.UserManagementService.model.User;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ExaminationServiceTest{
    @MockBean
    private ExaminationRepository examinationRepository;
    @MockBean
    private ReferralRepository referralRepository;
    @MockBean
    private Validator validator;

  @Autowired
    private MockMvc mockMvc;

  private Examination examination1 = new Examination(1L, "pacijent_uid1", "doktor_uid1", "dijagnozaaaa1", LocalDateTime.of(2024, Month.JANUARY, 3, 13, 43));
  private  Examination examination2 =     new Examination(2L, "pacijent_uid2", "doktor_uid2", "dijagnozaaaa2", LocalDateTime.of(2024, Month.JANUARY, 3, 14, 50));

  private List<Examination> mockExaminations = List.of(examination1, examination2);

   @Test
   public void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

  @Test
  public void getAllExaminations() throws Exception{
       when(examinationRepository.findAll()).thenReturn(mockExaminations);
       mockMvc.perform(MockMvcRequestBuilders.get("/api/examinations"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].dijagnoza", Matchers.is("dijagnozaaaa1")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].dijagnoza", Matchers.is("dijagnozaaaa2")));
  }

  
    @Test
    public void getExaminationByID_Success() throws Exception {
      Long id = 1L;
      when(examinationRepository.findById(id)).thenReturn(Optional.of(mockExaminations.get(0)));

      mockMvc.perform(MockMvcRequestBuilders.get("/api/examinations/{ID}", id))
              .andExpect(status().isOk())
              .andExpect(MockMvcResultMatchers.jsonPath("$.dijagnoza", Matchers.is("dijagnozaaaa1")));
    }

  @Test
  public void getExaminationByID_Fail() throws Exception {
    Long id = 1L;
    when(examinationRepository.findById(id)).thenReturn(Optional.of(mockExaminations.get(0)));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/examinations/{ID}", 2L))
            .andExpect(status().isNotFound());
  }
/*
  @Test
  public void createExamination_Success() throws Exception{
    
    String examinationJSON = "{\n"+
      "\"pacijent_uid\": \"pacijent_uid3\"," +
      "\"doktor_uid\": \"doktor_uid3\"," +
      "\"dijagnoza\": \"dijagnozaaaa3\"," +
      "\"termin_pregleda\": \"2024-01-03T10:15:30\"" +
      "}";

    when(examinationRepository.findAll()).thenReturn(mockExaminations);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/examinations")
                    .contentType(MediaType.APPLICATION_JSON).content(examinationJSON))
            .andExpect(status().isCreated());

    verify(examinationRepository, times(1)).save(any(Examination.class));
  }
*/
  @Test
  public void createExamination_Invalid() throws Exception{
    String examinationJSON = "{\n"+
      "\"pacijent_uid\": \"pacijent_uid3\",\n" +
      "\"doktor_uid\": \"doktor_uid3\",\n" +
      "\"dijagnoza\": \"neka dijagnozica\",\n" +
      "\"termin_pregleda\": \"2024-01-03T10:15:30\"\n" +
      "}";

    when(examinationRepository.findAll()).thenReturn(List.of(examination1, examination2));
      when(examinationRepository.save(any(Examination.class))).thenAnswer(invocation -> invocation.getArgument(0));


      mockMvc.perform(MockMvcRequestBuilders.post("/api/examinations")
                    .contentType(MediaType.APPLICATION_JSON).content(examinationJSON))
            .andExpect(status().isForbidden());

    verify(examinationRepository, never()).save(any(Examination.class));
  }

  @Test
    public void testDeleteExamination_Success() throws Exception {
        Long examinationId = 3L;

        Examination examination = new Examination(examinationId, "pacijent_uid3", "doktor_uid3", "dijagnozaaaa3", LocalDateTime.of(2024, Month.FEBRUARY, 21, 7, 35));


        when(examinationRepository.findById(examinationId)).thenReturn(Optional.of(examination));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/examinations/{ID}", examinationId))
                .andExpect(status().isOk()) 
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(examinationId.intValue())));

        verify(examinationRepository, times(1)).deleteById(examinationId);
    }

  @Test
    public void testDeleteExamination_NotFound() throws Exception {
        Long examinationId = 1L;

        when(examinationRepository.findById(examinationId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/examinations/{ID}", examinationId))
                .andExpect(status().isNotFound());

        verify(examinationRepository, never()).deleteById(anyLong());
    }

  @Test
	public void UpdateExamination_Success() throws Exception{
		String examinationJSON = "{\n"+
      "\"pacijent_uid\": \"pacijent_uid novi\",\n" +
      "\"doktor_uid\": \"doktor_uid novi \",\n" +
      "\"dijagnoza\": \"nova dijagnoza\",\n" +
      "\"termin_pregleda\": \"2024-01-03T10:15:30\"\n" +
      "}";
      Long id = 1L;
      when(examinationRepository.findById(id)).thenReturn(Optional.ofNullable(examination1));
      when(examinationRepository.save(any(Examination.class))).thenAnswer(invocation -> invocation.getArgument(0));


      mockMvc.perform(MockMvcRequestBuilders.put("/api/examinations/{ID}", id).
                    contentType(MediaType.APPLICATION_JSON).content(examinationJSON))
            .andExpect(status().isOk()) 
            .andExpect(MockMvcResultMatchers.jsonPath("$.dijagnoza", Matchers.is("nova dijagnoza")));

		verify(examinationRepository, times(1)).save(any(Examination.class));
	}

   @Test
	public void UpdateExamination_InvalidDijagnoza() throws Exception{
		String examinationJSON = "{\n"+
      "\"pacijent_uid\": \"pacijent_uid novi\",\n" +
      "\"doktor_uid\": \"doktor_uid novi \",\n" +
      "\"dijagnoza\": \"d\",\n" +
      "\"termin_pregleda\": \"2024-01-03T10:15:30\"\n" +
      "}";
		
		 when(examinationRepository.findById(1L)).thenReturn(Optional.of(examination1));
       when(examinationRepository.save(any(Examination.class))).thenAnswer(invocation -> invocation.getArgument(0));


       mockMvc.perform(MockMvcRequestBuilders.put("/api/examinations/{ID}", 1L).
                    contentType(MediaType.APPLICATION_JSON).content(examinationJSON))
            .andExpect(status().isForbidden()) 
            .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));

		verify(examinationRepository, never()).save(any(Examination.class));
	}

  @Test
	public void UpdateExaminationPartial_Success() throws Exception{
		String examinationJSON = "{\n"+
      "\"dijagnoza\": \"patch dijagnoza\"\n" +
      "}";
		
		 when(examinationRepository.findById(1L)).thenReturn(Optional.of(examination1));
      when(examinationRepository.save(any(Examination.class))).thenAnswer(invocation -> invocation.getArgument(0));


      mockMvc.perform(MockMvcRequestBuilders.patch("/api/examinations/{ID}", 1L).
                    contentType(MediaType.APPLICATION_JSON).content(examinationJSON))
            .andExpect(status().isOk()) 
            .andExpect(MockMvcResultMatchers.jsonPath("$.dijagnoza", Matchers.is("patch dijagnoza")));

		verify(examinationRepository, times(1)).save(any(Examination.class));
	}

   @Test
	public void UpdateExaminationPartial_Fail() throws Exception{
		String examinationJSON = "{\n"+
      "\"dijagnoza\": \"patch\"\n" +
      "}";
        when(examinationRepository.findById(1L)).thenReturn(Optional.ofNullable(examination1));
       when(examinationRepository.save(any(Examination.class))).thenAnswer(invocation -> invocation.getArgument(0));


       mockMvc.perform(MockMvcRequestBuilders.patch("/api/examinations/{ID}", 1L).
                    contentType(MediaType.APPLICATION_JSON).content(examinationJSON))
            .andExpect(status().isForbidden()) 
            .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));

		verify(examinationRepository, never()).save(any(Examination.class));
	}

  
  
  
}
