package ba.unsa.etf.nwt.UserManagementService.controllers;

import ba.unsa.etf.nwt.UserManagementService.model.Role;
import ba.unsa.etf.nwt.UserManagementService.model.User;
import ba.unsa.etf.nwt.UserManagementService.repositories.RoleRepository;
import ba.unsa.etf.nwt.UserManagementService.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mysql.cj.util.TestUtils;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    /*
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private List<User> mockUseri = List.of(
            new User(1L,
                    "Ime",
                    "Prezime",
                    LocalDate.of(2000, Month.JANUARY, 1),
                    0,
                    "123456789",
                    "ime@domain.com",
                    "password123",
                    "Adresa 123",
                    "img_path",
                    new Role(1L, "Doktor", false),
                    "UID-1234",
                    "BK-1234"),

            new User(2L,
                    "Drugi",
                    "Lik",
                    LocalDate.of(1980, Month.JUNE, 25),
                    1,
                    "123456789",
                    "drugi@domain.com",
                    "password123",
                    "Adresa 123",
                    "img_path",
                    new Role(2L, "Pacijent", false),
                    "UID-12345",
                    "BK-12345")
    );

    @Test
    public void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    public void getAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(mockUseri);
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ime", Matchers.is("Ime")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].ime", Matchers.is("Drugi")));
    }

    @Test
    public void getUserByID_Success() throws Exception {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(mockUseri.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/id/{ID}", id))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ime", Matchers.is("Ime")));
    }

    @Test
    public void getUserByID_Fail() throws Exception {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(mockUseri.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/id/{ID}", 2L))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
    }

    @Test
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
        Role rola = new Role(1L, "Doktor", false);
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
        Role rola = new Role(3L, "Nema", false);
        when(roleRepository.findBynazivRole(rola.getNazivRole())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/role/{nazivRole}", rola.getNazivRole()))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
    }

    @Test
    public void createUser_Success() throws Exception {
        User user = new User();
        user.setID(3L);
        user.setIme("John");
        user.setPrezime("Doe");
        user.setAdresa_stanovanja("123 Main St");
        user.setBroj_knjizice("123456789");
        user.setEmail("john@example.com");
        user.setDatum_rodjenja(LocalDate.now().minusYears(30));
        user.setPassword("password123");
        user.setBroj_telefona("123456789");
        user.setRola(new Role(1L, "Doktor", false));
        user.setSlika("img_path");
        user.setSpol(0);
        user.setUID("UID-132541");

        String userJSON = "{\n" +
                "  \"ime\": \"John\",\n" +
                "  \"prezime\": \"Doe\",\n" +
                "  \"datum_rodjenja\": \"1994-03-26\",\n" +
                "  \"spol\": 0,\n" +
                "  \"broj_telefona\": \"123456789\",\n" +
                "  \"email\": \"john@example.com\",\n" +
                "  \"password\": \"password123\",\n" +
                "  \"adresa_stanovanja\": \"123 Main St\",\n" +
                "  \"slika\": \"img_path\",\n" +
                "  \"rola\": {\n" +
                "    \"nazivRole\": \"Doktor\",\n" +
                "    \"id\": 1\n" +
                "  },\n" +
                "  \"broj_knjizice\": \"123456789\",\n" +
                "  \"id\": 3,\n" +
                "  \"uid\": \"UID-132541\"\n" +
                "}";

        when(userRepository.findAll()).thenReturn(mockUseri);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON).content(userJSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ime", Matchers.is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.prezime", Matchers.is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.adresa_stanovanja", Matchers.is("123 Main St")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.broj_knjizice", Matchers.is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("john@example.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.datum_rodjenja").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", Matchers.is("password123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.broj_telefona", Matchers.is("123456789")));

        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    public void createUser_WrongPassword() throws Exception {
        User user = new User();
        user.setID(3L);
        user.setIme("John");
        user.setPrezime("Doe");
        user.setAdresa_stanovanja("123 Main St");
        user.setBroj_knjizice("123456789");
        user.setEmail("john@example.com");
        user.setDatum_rodjenja(LocalDate.now().minusYears(30));
        user.setPassword("pass");
        user.setBroj_telefona("123456789");
        user.setRola(new Role(1L, "Doktor", false));
        user.setSlika("img_path");
        user.setSpol(0);
        user.setUID("UID-132541");

        String userJSON = "{\n" +
                "  \"ime\": \"John\",\n" +
                "  \"prezime\": \"Doe\",\n" +
                "  \"datum_rodjenja\": \"1994-03-26\",\n" +
                "  \"spol\": 0,\n" +
                "  \"broj_telefona\": \"123456789\",\n" +
                "  \"email\": \"john@example.com\",\n" +
                "  \"password\": \"pass\",\n" +
                "  \"adresa_stanovanja\": \"123 Main St\",\n" +
                "  \"slika\": \"img_path\",\n" +
                "  \"rola\": {\n" +
                "    \"nazivRole\": \"Doktor\",\n" +
                "    \"id\": 1\n" +
                "  },\n" +
                "  \"broj_knjizice\": \"123456789\",\n" +
                "  \"id\": 3,\n" +
                "  \"uid\": \"UID-132541\"\n" +
                "}";

        when(userRepository.findAll()).thenReturn(mockUseri);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON).content(userJSON))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
    }

    @Test
    public void createUser_WrongMail() throws Exception {
        User user = new User();
        user.setID(3L);
        user.setIme("John");
        user.setPrezime("Doe");
        user.setAdresa_stanovanja("123 Main St");
        user.setBroj_knjizice("123456789");
        user.setEmail("johnexample.com");
        user.setDatum_rodjenja(LocalDate.now().minusYears(30));
        user.setPassword("password123");
        user.setBroj_telefona("123456789");
        user.setRola(new Role(1L, "Doktor", false));
        user.setSlika("img_path");
        user.setSpol(0);
        user.setUID("UID-132541");

        String userJSON = "{\n" +
                "  \"ime\": \"John\",\n" +
                "  \"prezime\": \"Doe\",\n" +
                "  \"datum_rodjenja\": \"1994-03-26\",\n" +
                "  \"spol\": 0,\n" +
                "  \"broj_telefona\": \"123456789\",\n" +
                "  \"email\": \"johnexample.com\",\n" +
                "  \"password\": \"password123\",\n" +
                "  \"adresa_stanovanja\": \"123 Main St\",\n" +
                "  \"slika\": \"img_path\",\n" +
                "  \"rola\": {\n" +
                "    \"nazivRole\": \"Doktor\",\n" +
                "    \"id\": 1\n" +
                "  },\n" +
                "  \"broj_knjizice\": \"123456789\",\n" +
                "  \"id\": 3,\n" +
                "  \"uid\": \"UID-132541\"\n" +
                "}";

        when(userRepository.findAll()).thenReturn(mockUseri);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON).content(userJSON))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
    }

    @Test
    public void createUser_WrongNumber() throws Exception {
        User user = new User();
        user.setID(3L);
        user.setIme("John");
        user.setPrezime("Doe");
        user.setAdresa_stanovanja("123 Main St");
        user.setBroj_knjizice("123456789");
        user.setEmail("john@example.com");
        user.setDatum_rodjenja(LocalDate.now().minusYears(30));
        user.setPassword("password123");
        user.setBroj_telefona("12345a6789");
        user.setRola(new Role(1L, "Doktor", false));
        user.setSlika("img_path");
        user.setSpol(0);
        user.setUID("UID-132541");

        String userJSON = "{\n" +
                "  \"ime\": \"John\",\n" +
                "  \"prezime\": \"Doe\",\n" +
                "  \"datum_rodjenja\": \"1994-03-26\",\n" +
                "  \"spol\": 0,\n" +
                "  \"broj_telefona\": \"12345a6789\",\n" +
                "  \"email\": \"john@example.com\",\n" +
                "  \"password\": \"password123\",\n" +
                "  \"adresa_stanovanja\": \"123 Main St\",\n" +
                "  \"slika\": \"img_path\",\n" +
                "  \"rola\": {\n" +
                "    \"nazivRole\": \"Doktor\",\n" +
                "    \"id\": 1\n" +
                "  },\n" +
                "  \"broj_knjizice\": \"123456789\",\n" +
                "  \"id\": 3,\n" +
                "  \"uid\": \"UID-132541\"\n" +
                "}";

        when(userRepository.findAll()).thenReturn(mockUseri);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON).content(userJSON))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
    }

    @Test
    public void testChangeUserName_Success() throws Exception {
        Long userId = 1L;
        String newName = "NewName";

        User user = new User();
        user.setID(1L);
        user.setIme("John");
        user.setPrezime("Doe");
        user.setAdresa_stanovanja("123 Main St");
        user.setBroj_knjizice("123456789");
        user.setEmail("john@example.com");
        user.setDatum_rodjenja(LocalDate.now().minusYears(30));
        user.setPassword("password123");
        user.setBroj_telefona("123456789");
        user.setRola(new Role(1L, "Doktor", false));
        user.setSlika("img_path");
        user.setSpol(0);
        user.setUID("UID-132541");

        when(userRepository.getReferenceById(userId)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders.put("/users/change/{ID}/ime/{ime}", userId, newName))
                .andExpect(status().isOk()) // Expect status OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.ime", Matchers.is(newName)));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testChangeUserName_Failure_InvalidInput() throws Exception {
        Long userId = 1L;
        String invalidName = " ";

        mockMvc.perform(MockMvcRequestBuilders.put("/users/change/{ID}/ime/{ime}", userId, invalidName))
                .andExpect(status().isForbidden());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testDeleteUser_Success() throws Exception {
        Long userId = 1L;

        User user = new User();
        user.setID(1L);
        user.setIme("John");
        user.setPrezime("Doe");
        user.setAdresa_stanovanja("123 Main St");
        user.setBroj_knjizice("123456789");
        user.setEmail("john@example.com");
        user.setDatum_rodjenja(LocalDate.now().minusYears(30));
        user.setPassword("password123");
        user.setBroj_telefona("123456789");
        user.setRola(new Role(1L, "Doktor", false));
        user.setSlika("img_path");
        user.setSpol(0);
        user.setUID("UID-132541");


        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Perform DELETE request to delete a user
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete/{ID}", userId))
                .andExpect(status().isOk()) // Expect status OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(userId.intValue()))); // Expect returned user ID to match deleted user ID

        // Verify that userRepository.deleteById() was called with the correct user ID
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteUser_NotFound() throws Exception {
        Long userId = 1L;

        // Mock the behavior of UserRepository to return an empty optional, indicating user not found
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Perform DELETE request to delete a user
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete/{ID}", userId))
                .andExpect(status().isForbidden()); // Expect status Forbidden

        // Verify that userRepository.deleteById() was not called
        verify(userRepository, never()).deleteById(anyLong());
    }

     */
}