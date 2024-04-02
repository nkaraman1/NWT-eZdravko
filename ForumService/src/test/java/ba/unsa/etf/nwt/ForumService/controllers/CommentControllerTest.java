package ba.unsa.etf.nwt.ForumService.controllers;

import ba.unsa.etf.nwt.ForumService.DTO.CommentDTO;
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
public class CommentControllerTest {
    @MockBean
    private QuestionRepository questionRepository;
    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private MockMvc mockMvc;

    private List<Comment> mockComments = List.of(
            new Comment(2L,
                    new Question(1L, "1", "zdravlje", "savjeti za zdravlje", 0),
                    "2",
                    "odgovor",
                    0),

            new Comment(1L,
                    new Question(2L, "2", "sreca", "savjeti za srecu", 1),
                    "1",
                    "komentar",
                    1)
    );

    @Test
    public void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    public void getAllComments() throws Exception {
        when(commentRepository.findAll()).thenReturn(mockComments);
        mockMvc.perform(MockMvcRequestBuilders.get("/comments"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sadrzaj", Matchers.is("odgovor")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sadrzaj", Matchers.is("komentar")));
    }

    @Test
    public void getCommentByID_Success() throws Exception {
        Long id = 1L;
        when(commentRepository.findById(id)).thenReturn(Optional.of(mockComments.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/comments/{ID}", id))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sadrzaj", Matchers.is("odgovor")));
    }

    @Test
    public void getCommentByID_Fail() throws Exception {
        Long id = 1L;
        when(commentRepository.findById(id)).thenReturn(Optional.of(mockComments.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/comments/{ID}", 2L))
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
    public void createComment_Success() throws Exception {
        CommentDTO comment = new CommentDTO();
        comment.setQuestionId(2L);
        comment.setUserUid("1");
        comment.setSadrzaj("Cvjetic");
        comment.setAnonimnost(1);

        String commentJSON = "{\n" +
                "  \"questionId\": 2,\n" +
                "  \"userUid\": \"1\",\n" +
                "  \"sadrzaj\": \"Cvjetic\",\n" +
                "  \"anonimnost\": 1\n" +
                "}";

        when(commentRepository.findAll()).thenReturn(mockComments);

        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                        .contentType(MediaType.APPLICATION_JSON).content(commentJSON))
                .andExpect(status().isCreated());
                /*.andExpect(MockMvcResultMatchers.jsonPath("$.naslov", Matchers.is("Testic")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sadrzaj", Matchers.is("Cvjetic")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userUid", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.anonimnost", Matchers.is(1)));*/

        verify(questionRepository, times(1)).save(any(Question.class));

    }

    @Test
    public void createQuestion_WrongAnonimnost() throws Exception {
        Comment comment = new Comment();
        comment.setID(3L);
        comment.setUser_uid("1");
        comment.setPitanje(new Question(3L, "2", "naslov", "sadrzaj", 1));
        comment.setSadrzaj("Cvjetic");
        comment.setAnonimnost(5);

        String commentJSON = "{\n" +
                "  \"userUid\": \"1\",\n" +
                "  \"sadrzaj\": \"Cvjetic\",\n" +
                "  \"anonimnost\": 5\n" +
                "}";

        when(commentRepository.findAll()).thenReturn(mockComments);

        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                        .contentType(MediaType.APPLICATION_JSON).content(commentJSON))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
    }

    @Test
    public void testDeleteComment_Success() throws Exception {
        Long commentId = 3L;

        Comment comment = new Comment();
        comment.setID(3L);
        comment.setUser_uid("1");
        comment.setPitanje(new Question(3L, "2", "naslov", "sadrzaj", 1));
        comment.setSadrzaj("Cvjetic");
        comment.setAnonimnost(1);


        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Perform DELETE request to delete a user
        mockMvc.perform(MockMvcRequestBuilders.delete("/comments/{ID}", commentId))
                .andExpect(status().isOk()) // Expect status OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(commentId.intValue())));

        // Verify that userRepository.deleteById() was called with the correct user ID
        verify(commentRepository, times(1)).deleteById(commentId);
    }

    @Test
    public void testDeleteComment_NotFound() throws Exception {
        Long commentId = 1L;

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/comments/{ID}", commentId))
                .andExpect(status().isNotFound());

        verify(commentRepository, never()).deleteById(anyLong());
    }
}