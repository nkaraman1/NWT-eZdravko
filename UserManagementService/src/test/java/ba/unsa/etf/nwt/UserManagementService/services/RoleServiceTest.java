package ba.unsa.etf.nwt.UserManagementService.services;

import ba.unsa.etf.nwt.UserManagementService.model.Role;
import ba.unsa.etf.nwt.UserManagementService.repositories.RoleRepository;
import ba.unsa.etf.nwt.UserManagementService.repositories.UserRepository;
import net.bytebuddy.matcher.StringMatcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RoleServiceTest {
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    public void getAllRoles() throws Exception {
        when(roleRepository.findAll()).thenReturn(List.of(new Role("Doktor", false), new Role("Pacijent", false)));
        mockMvc.perform(MockMvcRequestBuilders.get("/roles"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nazivRole", Matchers.is("Doktor")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nazivRole", Matchers.is("Pacijent")));
    }

    @Test
    public void getRoleByID_Success() throws Exception {
        Long id = new Long(1);
        when(roleRepository.findById(id)).thenReturn(Optional.of(new Role(id, "Doktor", false)));
        mockMvc.perform(MockMvcRequestBuilders.get("/roles/id/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nazivRole", Matchers.is("Doktor")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
    }

    @Test
    public void getRoleByID_Fail() throws Exception {
        Long id = new Long(1);
        when(roleRepository.findById(id)).thenReturn(Optional.of(new Role(id, "Doktor", false)));
        mockMvc.perform(MockMvcRequestBuilders.get("/roles/id/{id}", 2))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
    }

    @Test
    public void getRoleByName_Success() throws Exception {
        String roleName = "Doktor";
        when(roleRepository.findBynazivRole(roleName)).thenReturn(Optional.of(new Role(1L, roleName, false)));
        mockMvc.perform(MockMvcRequestBuilders.get("/roles/name/{nazivRole}", roleName))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nazivRole", Matchers.is(roleName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
    }

    @Test
    public void getRoleByName_Fail() throws Exception {
        String roleName = "Doktor";
        when(roleRepository.findBynazivRole(roleName)).thenReturn(Optional.of(new Role(1L, roleName, false)));
        mockMvc.perform(MockMvcRequestBuilders.get("/roles/name/{nazivRole}", "NijeDoktor"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
    }

    @Test
    public void createRole_Success() throws Exception {
        when(roleRepository.findAll()).thenReturn(List.of(new Role("Doktor", false), new Role("Pacijent", false)));
        mockMvc.perform(MockMvcRequestBuilders.post("/roles/create/{nazivRole}/flag-kod/{potrebanKod}", "Admin", true))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nazivRole", Matchers.is("Admin")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.kod", Matchers.hasLength(10)));

        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    public void createRole_Fail() throws Exception {
        when(roleRepository.findAll()).thenReturn(List.of(new Role("Doktor", false), new Role("Pacijent", false)));
        when(roleRepository.findBynazivRole("Doktor")).thenReturn(Optional.of(new Role(1L,"Doktor", false)));
        mockMvc.perform(MockMvcRequestBuilders.post("/roles/create/{nazivRole}/flag-kod/{potrebanKod}", "Doktor", false))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
    }

    @Test
    public void deleteRole_Success() throws Exception {
        Long roleId = 1L;

        Role roleToDelete = new Role(roleId, "Rola", false);
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roleToDelete));
        when(userRepository.findByRola(roleToDelete)).thenReturn(List.of());
        doNothing().when(roleRepository).deleteById(roleId);

        mockMvc.perform(delete("/roles/delete/{id}", roleId))
                .andExpect(status().isOk());

        verify(roleRepository, times(1)).deleteById(roleId);
    }

    @Test
    public void deleteRole_Fail() throws Exception {
        Long roleId = 1L;

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/roles/delete/{id}", roleId))
                .andExpect(status().isForbidden()); // Expect status Forbidden

        verify(roleRepository, never()).deleteById(anyLong());
    }

    @Test
    public void updateCode_Success() throws Exception {
        Long roleId = 1L;
        String stariKod = "-nekikod--"; // Naštiman je kod koji je nemoguće generisat

        Role roleToUpdate = new Role(roleId, "Rola", true, stariKod);
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roleToUpdate));

        mockMvc.perform(patch("/roles/update-code/{id}", roleId))
                .andExpect(status().isOk());

        ArgumentCaptor<Role> roleCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleRepository, times(1)).save(roleCaptor.capture());
        assertNotEquals(stariKod, roleCaptor.getValue().getKod());
    }

    @Test
    public void updateCode_Fail() throws Exception {
        Long roleId = 1L;

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/roles/update-code/{id}", roleId))
                .andExpect(status().isForbidden()); // Expect status Forbidden
    }
}