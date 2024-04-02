package ba.unsa.etf.nwt.UserManagementService.controllers;

import ba.unsa.etf.nwt.UserManagementService.model.Role;
import ba.unsa.etf.nwt.UserManagementService.repositories.RoleRepository;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RoleControllerTest {
    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    public void getAllRoles() throws Exception {
        when(roleRepository.findAll()).thenReturn(List.of(new Role("Doktor"), new Role("Pacijent")));
        mockMvc.perform(MockMvcRequestBuilders.get("/roles"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nazivRole", Matchers.is("Doktor")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nazivRole", Matchers.is("Pacijent")));
    }

    @Test
    public void getRoleByID_Success() throws Exception {
        Long id = new Long(1);
        when(roleRepository.findById(id)).thenReturn(Optional.of(new Role(id, "Doktor")));
        mockMvc.perform(MockMvcRequestBuilders.get("/roles/id/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nazivRole", Matchers.is("Doktor")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
    }

    @Test
    public void getRoleByID_Fail() throws Exception {
        Long id = new Long(1);
        when(roleRepository.findById(id)).thenReturn(Optional.of(new Role(id, "Doktor")));
        mockMvc.perform(MockMvcRequestBuilders.get("/roles/id/{id}", 2))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
    }

    @Test
    public void getRoleByName_Success() throws Exception {
        String roleName = "Doktor";
        when(roleRepository.findBynazivRole(roleName)).thenReturn(Optional.of(new Role(1L, roleName)));
        mockMvc.perform(MockMvcRequestBuilders.get("/roles/name/{nazivRole}", roleName))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nazivRole", Matchers.is(roleName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
    }

    @Test
    public void getRoleByName_Fail() throws Exception {
        String roleName = "Doktor";
        when(roleRepository.findBynazivRole(roleName)).thenReturn(Optional.of(new Role(1L, roleName)));
        mockMvc.perform(MockMvcRequestBuilders.get("/roles/name/{nazivRole}", "NijeDoktor"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
    }

    @Test
    public void createRole_Success() throws Exception {
        when(roleRepository.findAll()).thenReturn(List.of(new Role("Doktor"), new Role("Pacijent")));
        mockMvc.perform(MockMvcRequestBuilders.post("/roles/create/{nazivRole}", "Admin"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nazivRole", Matchers.is("Admin")));

        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    public void createRole_Fail() throws Exception {
        when(roleRepository.findAll()).thenReturn(List.of(new Role("Doktor"), new Role("Pacijent")));
        when(roleRepository.findBynazivRole("Doktor")).thenReturn(Optional.of(new Role(1L,"Doktor")));
        mockMvc.perform(MockMvcRequestBuilders.post("/roles/create/{nazivRole}", "Doktor"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")));
    }

    /*@Disabled
    @Test
    public void deleteRole_Success() throws Exception {
        Long roleId = 1L;

        // Mock the behavior of RoleRepository
        Role roleToDelete = new Role(roleId, "Doktor");
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roleToDelete));
        //doNothing().when(roleRepository).deleteById(roleId);

        // Perform DELETE request to delete a role
        mockMvc.perform(delete("/roles/delete/{id}", roleId))
                .andExpect(status().isOk());

        // Verify that roleRepository.deleteById() was called with the correct role ID
        verify(roleRepository, times(1)).deleteById(roleId);
    }*/

    @Test
    public void deleteRole_Fail() throws Exception {
        Long roleId = 1L;

        // Mock the behavior of RoleRepository to return an empty optional, indicating role not found
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Perform DELETE request to delete a role
        mockMvc.perform(delete("/roles/delete/{id}", roleId))
                .andExpect(status().isForbidden()); // Expect status Forbidden

        // Verify that roleRepository.deleteById() was not called
        verify(roleRepository, never()).deleteById(anyLong());
    }
}