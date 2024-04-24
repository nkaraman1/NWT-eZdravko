@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ExaminationServiceTest{
  private final ExaminationRepository examinationRepository;
  private final ReferralRepository referralRepository;
  private final Validator validator;

  @Autowired
    private MockMvc mockMvc;

  private List<Examinations> mockExaminations = List.of(
    new Examination(1L, "pacijent_uid1", "doktor_uid1", "dijagnozaaaa1", LocalDateTime termin_pregleda),
    new Examination(2L, "pacijent_uid2", "doktor_uid2", "dijagnozaaaa2", LocalDateTime termin_pregleda)
  );

   @Test
    public void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

  @Test
  public void getAllExaminations() throws Exception{
     when(examinationRepository.findAll()).thenReturn(mocExaminations);
        mockMvc.perform(MockMvcRequestBuilders.get("/examinations"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dijagnoza", Matchers.is("dijagnozaaaa1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dijagnoza", Matchers.is("dijagnozaaaa2")));
  }

  
    @Test
    public void getExaminationByID_Success() throws Exception {
      Long id = 1L;
      when(examinationRepository.findById(id)).thenReturn(Optional.of(mockExaminations.get(0)));

      mockMvc.perform(MockMvcRequestBuilders.get("/examinations/{ID}", id))
              .andExpect(status().isOk())
              .andExpect(MockMvcResultMatchers.jsonPath("$.dijagnoza", Matchers.is("dijagnozaaaa1")));
    }

  @Test
  public void getExaminationByID_Fail() throws Exception {
    Long id = 1L;
    when(examinationRepository.findById(id)).thenReturn(Optional.of(mockExaminations.get(0)));

    mockMvc.perform(MockMvcRequestBuilders.get("/examinations/{ID}", 2L))
            .andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("not_found")));
  }

  @Test
  public void createExamination_Success() throws Exception{
    
    String examinationJSON = "{\n"+
      "\"pacijent_uid\": \"pacijent_uid3\"," +
      "\"doktor_uid\": \"doktor_uid3\"," +
      "\"dijagnoza\": \"dijagnozaaaa3\"," +
      "\termin_pregeda\": datum" +
      "}";

    when(examinationRepository.findAll()).thenReturn(mockExaminations);

    mockMvc.perform(MockMvcRequestBuilders.post("/examinations")
                    .contentType(MediaType.APPLICATION_JSON).content(examinationJSON))
            .andExpect(status().isCreated());

    verify(examinationRepository, times(1)).save(any(Examination.class));
  }

  @Test
  public void createExamination_Invalid() throws Exception{
    String examinationJSON = "{\n"+
      "\"pacijent_uid\": \"pacijent_uid3\"," +
      "\"doktor_uid\": \"doktor_uid3\"," +
      "\"dijagnoza\": \"d3\"," +
      "\termin_pregeda\": datum" +
      "}";

    when(examinationRepository.findAll()).thenReturn(mockExaminations);

    mockMvc.perform(MockMvcRequestBuilders.post("/examinations")
                    .contentType(MediaType.APPLICATION_JSON).content(examinationJSON))
            .andExpect(status().isForbidden())
            .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));

    verify(examinationRepository, never()).save(any(Examination.class));
  }

  @Test
    public void testDeleteExamination_Success() throws Exception {
        Long examinationId = 3L;

        Examination examination = new Examination(examinationId, "pacijent_uid3", "doktor_uid3", "dijagnozaaaa3", LocalDateTime termin_pregleda); 


        when(examinationRepository.findById(examinationId)).thenReturn(Optional.of(examination));

        mockMvc.perform(MockMvcRequestBuilders.delete("/examinations/{ID}", examinationId))
                .andExpect(status().isOk()) 
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(examinationId.intValue())));

        verify(examinationRepository, times(1)).deleteById(examinationId);
    }

  @Test
    public void testDeleteExamination_NotFound() throws Exception {
        Long examinationId = 1L;

        when(examinationRepository.findById(examinationId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/examinations/{ID}", examinationId))
                .andExpect(status().isNotFound());

        verify(examinationRepository, never()).deleteById(anyLong());
    }

  @Test
	public void UpdateExamination_Success() throws Exception{
		String examinationJSON = "{\n"+
      "\"pacijent_uid\": \"pacijent_uid novi\"," +
      "\"doktor_uid\": \"doktor_uid novi \"," +
      "\"dijagnoza\": \"nova dijagnoza\"," +
      "\termin_pregeda\": datum" +
      "}";
		
		 when(examinationRepository.getReferenceById(1L)).thenReturn(mockExaminations.get(0));
		
    mockMvc.perform(MockMvcRequestBuilders.put("/examinations/{ID}", 1L).
                    contentType(MediaType.APPLICATION_JSON).content(examinationJSON))
            .andExpect(status().isOk()) 
            .andExpect(MockMvcResultMatchers.jsonPath("$.dijagnoza", Matchers.is("nova dijagnoza")));

		verify(examinationRepository, times(1)).save(any(Examination.class));
	}

   @Test
	public void UpdateExamination_InvalidDijagnoza() throws Exception{
		String examinationJSON = "{\n"+
      "\"pacijent_uid\": \"pacijent_uid novi\"," +
      "\"doktor_uid\": \"doktor_uid novi \"," +
      "\"dijagnoza\": \"d\"," +
      "\termin_pregeda\": datum" +
      "}";
		
		 when(examinationRepository.getReferenceById(1L)).thenReturn(mockExaminations.get(0));
		
    mockMvc.perform(MockMvcRequestBuilders.put("/examinations/{ID}", 1L).
                    contentType(MediaType.APPLICATION_JSON).content(examinationJSON))
            .andExpect(status().isForbidden()) 
            .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));

		verify(examinationRepository, never()).save(any(Examination.class));
	}

  @Test
	public void UpdateExaminationPartial_Success() throws Exception{
		String examinationJSON = "{\n"+
      "\"dijagnoza\": \"patch dijagnoza\"," +
      "}";
		
		 when(examinationRepository.getReferenceById(1L)).thenReturn(mockExaminations.get(0));
		
    mockMvc.perform(MockMvcRequestBuilders.patch("/examinations/{ID}", 1L).
                    contentType(MediaType.APPLICATION_JSON).content(examinationJSON))
            .andExpect(status().isOk()) 
            .andExpect(MockMvcResultMatchers.jsonPath("$.dijagnoza", Matchers.is("patch dijagnoza")));

		verify(examinationRepository, times(1)).save(any(Examination.class));
	}

   @Test
	public void UpdateExaminationPartial_Fail() throws Exception{
		String examinationJSON = "{\n"+
      "\"dijagnoza\": \"patch\"," +
      "}";
		
		 when(examinationRepository.getReferenceById(1L)).thenReturn(mockExaminations.get(0));
		
    mockMvc.perform(MockMvcRequestBuilders.patch("/examinations/{ID}", 1L).
                    contentType(MediaType.APPLICATION_JSON).content(examinationJSON))
            .andExpect(status().isForbidden()) 
            .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));

		verify(examinationRepository, never()).save(any(Examination.class));
	}

  
  
  
}
