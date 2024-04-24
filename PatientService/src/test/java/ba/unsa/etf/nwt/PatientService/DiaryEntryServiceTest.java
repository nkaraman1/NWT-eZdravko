@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DiaryEntryServiceTest{
	@MockBean
	private DiaryEntryRepository diaryEntryRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	private List<DiaryEntry> mockDiaryEntries = List.of(
		new DiaryEntry(1L, 'user_UID', datum, 166.5, 59.5, 23, 92, 1, 2345),
		new DiaryEntry(2L, 'user_UID', datum, 166.5, 60, 23, 67, 1.5, 2468)
	);

	@Test
	public void shouldCreateMockMvc(){
		assertNotNull(mockMvc);	
	}

	@Test
	public void getAllDiaryEntries() throws Exception{
		when(diaryEntryRepository.findAll()).thenReturn(mockDiaryEntries);
		mockMvc.perform(MockMvcRequestBuilders.get("/diaryEntries"))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
			.andExpect(MockMvcResultMatchers.jsonPath("$[0].broj_koraka", Matchers.is(2345)))
			.andExpect(MockMvcResultMatchers.jsonPath("$[1].broj_koraka", Matchers.is(2468)));
	}

	@Test
	public void getDiaryEntryByID_Success() throws Exception{
		Long id = 1L;
		when(diaryEntryRepository.findById(id)).thenReturn(Optional.of(mockDiaryEntries.get(0)));

		mockMvc.perform(MockMvcRequestBuilders.get("/diary-entries/{ID}", id))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.broj_koraka", Matchers.is(2345)));
	}

	@Test
	public void getDiaryEntryByID_Fail() throws Exception {
		Long id = 1L;
		when(diaryEntryRepository.findById(id)).thenReturn(Optional.of(mockDiaryEntries.get(0)));

		mockMvc.perform(MockMvcRequestBuilders.get("/diary-entries/{ID}", 2L))
			.andExpect(status().isNotFound())
			.andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("not_found")));
	}

	@Test
	public void CreateDiaryEntry_Success() throws Exception{
		DiaryEntryDTO diaryEntryDTO = new DiaryEntryDTO();
		diaryEntryDTO.setUser_uid("user_uid");
		diaryEntryDTO.setDatum(datum);
		diaryEntryDTO.setVisina(166);
		diaryEntryDTO.setTezina(59);
		diaryEntryDTO.setBmi(24);
		diaryEntryDTO.setPuls(66);
		diaryEntryDTO.setUnos_vode(1.5);
		diaryEntryDTO.setBroj_koraka(1234);	

		String diaryEntryJSON = "{\n" +
			"\"user_uid\": \"user_uid\", \n" +
			"\"datum\": datum, \n" +
			"\"visina\": 166, \n" +
			"\"tezina\": 59, \n" +
			"\"bmi\": 24, \n" +
			"\"puls\": 66, \n" +
			"\"unos_vode\": 1.5, \n" +
			"\"broj_koraka\": 1234, \n" +
			"}"
		when(diaryEntryRepository.findAll()).thenReturn(mockDiaryEntries);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/diary-entries")
				.contentType(MediaType.APPLICATION_JSON).content(diaryEntryJSON))
			.andExpect(status().isCreated());

	}

	@Test
	public void CreateDiaryEntry_InvalidVisina() throws Exception{
		DiaryEntryDTO diaryEntryDTO = new DiaryEntryDTO();
		diaryEntryDTO.setUser_uid("user_uid");
		diaryEntryDTO.setDatum(datum);
		diaryEntryDTO.setVisina(-166);
		diaryEntryDTO.setTezina(59);
		diaryEntryDTO.setBmi(24);
		diaryEntryDTO.setPuls(66);
		diaryEntryDTO.setUnos_vode(1.5);
		diaryEntryDTO.setBroj_koraka(1234);	

		String diaryEntryJSON = "{\n" +
			"\"user_uid\": \"user_uid\", \n" +
			"\"datum\": datum, \n" +
			"\"visina\": -166, \n" +
			"\"tezina\": 59, \n" +
			"\"bmi\": 24, \n" +
			"\"puls\": 66, \n" +
			"\"unos_vode\": 1.5, \n" +
			"\"broj_koraka\": 1234, \n" +
			"}"
		when(diaryEntryRepository.findAll()).thenReturn(mockDiaryEntries);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/diary-entries")
				.contentType(MediaType.APPLICATION_JSON).content(diaryEntryJSON))
			.andExpect(status().isForbidden())
			.andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));

		verify(diaryEntryRepository, times(1)).save(any(DiaryEntry.class));	

	}

	@Test
	public void DeleteDiaryEntry_Success() throws Exception{
		Long diaryEntryID = 3L;
		DiaryEntry diaryEntry = new DiaryEntry(diaryEntryID, 'user_UID', datum, 166.5, 60, 23, 67, 2, 2468);
		when(diaryEntryRepository.findById(diaryEntryID)).thenReturn(Optional.of(diaryEntry));
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/diary-entries/{ID}", diaryEntryID))
			.andExpect(status().isOk())
			.andExpect(MovkMvcResultMatcher.jsonPath("$.id", Matchers.is(3)));

		verify(diaryEntryRepository, times(1)).deleteById(diaryEntryID);		
		
	}

	@Test
	public void DeleteDiaryEntry_NotFound() throws Exception{
		Long diaryEntryID = 1L;
		when(diaryEntryRepository.findById(diaryEntryID)).thenReturn(Optional.empty());
		
		mockMvc.perform(MockMvcRequetBuilders.delete("diary-entries/{ID}", diaryEntryID))
			.andExpect(status().isNotFound());

		verify(diaryEntryRepository, never()).deleteById(anyLong());
	}

	@Test
	public void UpdateDiaryEntry_Success() throws Exception{
		String diaryEntryJSON = "{\n" +
			"\"user_uid\": \"user_uid\", \n" +
			"\"datum\": datum, \n" +
			"\"visina\": 166, \n" +
			"\"tezina\": 60, \n" +
			"\"bmi\": 23, \n" +
			"\"puls\": 66, \n" +
			"\"unos_vode\": 1.5, \n" +
			"\"broj_koraka\": 1234, \n" +
			"}";
		
		 when(diaryEntryRepository.getReferenceById(1L)).thenReturn(mockDiaryEntries.get(0));
		
	        mockMvc.perform(MockMvcRequestBuilders.put("/diary-entries/{ID}", 1L).
	                        contentType(MediaType.APPLICATION_JSON).content(diaryEntryJSON))
	                .andExpect(status().isOk())
	                .andExpect(MockMvcResultMatchers.jsonPath("$.tezina", Matchers.is(60)));

		verify(diaryEntryRepository, times(1)).save(any(DiaryEntry.class));
	}

	@Test
	public void UpdateDiaryEntry_InvalidVisina() throws Exception{
		String diaryEntryJSON = "{\n" +
			"\"user_uid\": \"user_uid\", \n" +
			"\"datum\": datum, \n" +
			"\"visina\": -166, \n" +
			"\"tezina\": 60, \n" +
			"\"bmi\": 23, \n" +
			"\"puls\": 66, \n" +
			"\"unos_vode\": 1.5, \n" +
			"\"broj_koraka\": 1234, \n" +
			"}";
		
		 when(diaryEntryRepository.getReferenceById(1L)).thenReturn(mockDiaryEntries.get(0));
		
	        mockMvc.perform(MockMvcRequestBuilders.put("/diary-entries/{ID}", 1L)
				.contentType(MediaType.APPLICATION_JSON).content(diaryEntryJSON))
			.andExpect(status().isForbidden())
			.andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));

		verify(diaryEntryRepository, never()).save(any(DiaryEntry.class));	
	}

	@Test
	public void UpdateDiaryEntryPartial_Success() throws Exception{
		String diaryEntryJSON = "{\n" +
			"\"tezina\": 80, \n" +
			"}";
		
		 when(diaryEntryRepository.getReferenceById(1L)).thenReturn(mockDiaryEntries.get(0));
		
	        mockMvc.perform(MockMvcRequestBuilders.patch("/diary-entries/{ID}", 1L).
	                        contentType(MediaType.APPLICATION_JSON).content(diaryEntryJSON))
	                .andExpect(status().isOk()) 
	                .andExpect(MockMvcResultMatchers.jsonPath("$.tezina", Matchers.is(80)));

		verify(diaryEntryRepository, times(1)).save(any(DiaryEntry.class));
	}

	@Test
	public void UpdateDiaryEntryPartial_InvalidTezina() throws Exception{
		String diaryEntryJSON = "{\n" +
			"\"tezina\": -80, \n" +
			"}";
		
		 when(diaryEntryRepository.getReferenceById(1L)).thenReturn(mockDiaryEntries.get(0));
		
	        mockMvc.perform(MockMvcRequestBuilders.patch("/diary-entries/{ID}", 1L).
	                        contentType(MediaType.APPLICATION_JSON).content(diaryEntryJSON))
	                .andExpect(status().isForbidden())
			.andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));

		verify(diaryEntryRepository, never()).save(any(DiaryEntry.class));
	}



}
