package ba.unsa.etf.nwt.ForumService.controllers;

import ba.unsa.etf.nwt.ForumService.model.Comment;
import ba.unsa.etf.nwt.ForumService.model.Question;
import ba.unsa.etf.nwt.ForumService.repositories.CommentRepository;
import ba.unsa.etf.nwt.ForumService.repositories.QuestionRepository;
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

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class QuestionControllerTest {
    @MockBean
    private QuestionRepository questionRepository;
    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private MockMvc mockMvc;

    private List<Question> mockQuestions = List.of(
            new Question(1L,
                    "1",
                    "Ishrana",
                    "Koliki je preporučeni dnevni unos kalorija?",
                    1),

            new Question(2L,
                    "2",
                    "Slatkiši",
                    "Zdravi slatkiši???",
                    0)
    );

    @Test
    public void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    public void getAllQuestions() throws Exception {
        when(questionRepository.findAll()).thenReturn(mockQuestions);
        mockMvc.perform(MockMvcRequestBuilders.get("/questions"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].naslov", Matchers.is("Ishrana")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].naslov", Matchers.is("Slatkiši")));
    }

    @Test
    public void getQuestionByID_Success() throws Exception {
        Long id = 1L;
        when(questionRepository.findById(id)).thenReturn(Optional.of(mockQuestions.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/questions/{ID}", id))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.naslov", Matchers.is("Ishrana")));
    }

    @Test
    public void getQuestionByID_Fail() throws Exception {
        Long id = 1L;
        when(questionRepository.findById(id)).thenReturn(Optional.of(mockQuestions.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/questions/{ID}", 2L))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("not_found")));
    }

    /*@Test
    public void getUserByImePrezime_Success() throws Exception {
        String ime = "Ime";
        String prezime = "Prezime";
        when(userRepository.findByImeAndPrezime(ime, prezime)).thenReturn(List.of(mockUseri.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/ime/{ime}/prezime/{prezime}", ime, prezime))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ime", Matchers.is("Ime")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].prezime", Matchers.is("Prezime")));
    }

    @Test
    public void getUserByImePrezime_Fail() throws Exception {
        String ime = "Nepoznat";
        String prezime = "Lik";
        when(userRepository.findByImeAndPrezime(ime, prezime)).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/ime/{ime}/prezime/{prezime}", ime, prezime))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(0)));
    }

    @Test
    public void getUsersByRola_Success() throws Exception {
        Role rola = new Role(1L, "Doktor");
        when(roleRepository.findBynazivRole(rola.getNazivRole())).thenReturn(Optional.of(rola));
        when(userRepository.findByRola(rola)).thenReturn(List.of(mockUseri.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/role/{nazivRole}", rola.getNazivRole()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ime", Matchers.is("Ime")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].prezime", Matchers.is("Prezime")));
    }

    @Test
    public void getUsersByRola_Fail() throws Exception {
        Role rola = new Role(3L, "Nema");
        when(roleRepository.findBynazivRole(rola.getNazivRole())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/role/{nazivRole}", rola.getNazivRole()))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
    }*/

    @Test
    public void createQuestion_Success() throws Exception {
        Question question = new Question();
        question.setID(3L);
        question.setUser_uid("1");
        question.setNaslov("Testic");
        question.setSadrzaj("Cvjetic");
        question.setAnonimnost(1);

        String questionJSON = "{\n" +
                "  \"userUid\": \"1\",\n" +
                "  \"naslov\": \"Testic\",\n" +
                "  \"sadrzaj\": \"Cvjetic\",\n" +
                "  \"anonimnost\": 1\n" +
                "}";

        when(questionRepository.findAll()).thenReturn(mockQuestions);

        mockMvc.perform(MockMvcRequestBuilders.post("/questions")
                        .contentType(MediaType.APPLICATION_JSON).content(questionJSON))
                .andExpect(status().isCreated());
                /*.andExpect(MockMvcResultMatchers.jsonPath("$.naslov", Matchers.is("Testic")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sadrzaj", Matchers.is("Cvjetic")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userUid", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.anonimnost", Matchers.is(1)));*/

        verify(questionRepository, times(1)).save(any(Question.class));

    }

    @Test
    public void createQuestion_WrongAnonimnost() throws Exception {
        Question question = new Question();
        question.setID(3L);
        question.setUser_uid("1");
        question.setNaslov("Testic");
        question.setSadrzaj("Cvjetic");
        question.setAnonimnost(5);

        String questionJSON = "{\n" +
                "  \"userUid\": \"1\",\n" +
                "  \"naslov\": \"Testic\",\n" +
                "  \"sadrzaj\": \"Cvjetic\",\n" +
                "  \"anonimnost\": 5\n" +
                "}";

        when(questionRepository.findAll()).thenReturn(mockQuestions);

        mockMvc.perform(MockMvcRequestBuilders.post("/questions")
                        .contentType(MediaType.APPLICATION_JSON).content(questionJSON))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
    }

    @Test
    public void testDeleteQuestion_Success() throws Exception {
        Long questionId = 3L;

        Question question = new Question();
        question.setID(3L);
        question.setUser_uid("1");
        question.setNaslov("Testic");
        question.setSadrzaj("Cvjetic");
        question.setAnonimnost(1);


        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        // Perform DELETE request to delete a user
        mockMvc.perform(MockMvcRequestBuilders.delete("/questions/{ID}", questionId))
                .andExpect(status().isOk()) // Expect status OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(questionId.intValue())));

        // Verify that userRepository.deleteById() was called with the correct user ID
        verify(questionRepository, times(1)).deleteById(questionId);
    }

    @Test
    public void testDeleteQuestion_NotFound() throws Exception {
        Long questionId = 1L;

        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/questions/{ID}", questionId))
                .andExpect(status().isNotFound());

        verify(questionRepository, never()).deleteById(anyLong());
    }
}