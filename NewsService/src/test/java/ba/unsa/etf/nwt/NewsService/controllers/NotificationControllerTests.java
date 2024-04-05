package ba.unsa.etf.nwt.NewsService.controllers;

import ba.unsa.etf.nwt.NewsService.DTO.NotificationDTO;
import ba.unsa.etf.nwt.NewsService.model.Notification;
import ba.unsa.etf.nwt.NewsService.repositories.NotificationsRepository;
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
public class NotificationControllerTests {

    @MockBean
    private NotificationsRepository notificationsRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    public void getNotifications_Success() throws Exception {
        // Mock the behavior of NotificationsRepository
        when(notificationsRepository.findAll()).thenReturn(List.of(new Notification(1L, "Type", "Content", "UserUID")));

        // Perform GET request to fetch all notifications
        mockMvc.perform(MockMvcRequestBuilders.get("/notifications"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tip_notifikacije", Matchers.is("Type")));
    }

    @Test
    public void getNotifications_EmptyList_Success() throws Exception {
        // Mock the behavior of NotificationsRepository
        when(notificationsRepository.findAll()).thenReturn(Collections.emptyList());

        // Perform GET request when no notifications are found
        mockMvc.perform(MockMvcRequestBuilders.get("/notifications"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(0)));
    }

    @Test
    public void createNotification_Success() throws Exception {
        // Mock the behavior of NotificationsRepository
        when(notificationsRepository.save(any(Notification.class))).thenReturn(new Notification(1L, "Type", "Content", "UserUID"));

        // Create a notification DTO for the request body
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setTip_notifikacije("Type");
        notificationDTO.setSadrzaj("Content");
        notificationDTO.setUser_uid("UserUID");

        // Perform POST request to create a new notification
        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(notificationDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tip_notifikacije", Matchers.is("Type")));

        // Verify that notificationsRepository.save() was called with the correct Notification object
        verify(notificationsRepository, times(1)).save(any(Notification.class));
    }

    @Test
    public void createNotification_InvalidInput_Fail() throws Exception {
        // Prepare invalid notification data
        NotificationDTO notificationDTO = new NotificationDTO();

        // Perform the POST request
        MvcResult result = mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(notificationDTO)))
                .andExpect(status().isForbidden()) // Expecting 403
                .andReturn();

        // Extract the response body as a String
        String responseBody = result.getResponse().getContentAsString();

        // Assert the response body contains the expected error messages
        assertThat(responseBody).contains("Tip notifikacije je obavezan");
        assertThat(responseBody).contains("SadrÅ¾aj notifikacije je obavezan");
        assertThat(responseBody).contains("UID korisnika je obavezan");
    }

    @Test
    public void getNotificationByID_Success() throws Exception {
        // Mock the behavior of NotificationsRepository
        Notification notification = new Notification(1L, "Type", "Content", "UserUID");
        when(notificationsRepository.findById(1L)).thenReturn(Optional.of(notification));

        // Perform GET request to fetch notification by ID
        mockMvc.perform(MockMvcRequestBuilders.get("/notifications/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tip_notifikacije", Matchers.is("Type")));
    }

    @Test
    public void getNotificationByID_NotFound() throws Exception {
        // Mock the behavior of NotificationsRepository
        when(notificationsRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform GET request with non-existing notification ID
        mockMvc.perform(MockMvcRequestBuilders.get("/notifications/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Nije pronadjena notifikacija sa tim ID-em.")));
    }

    @Test
    public void deleteNotification_Success() throws Exception {
        // Mock the behavior of NotificationsRepository
        Notification notification = new Notification(1L, "Type", "Content", "UserUID");
        when(notificationsRepository.findById(1L)).thenReturn(Optional.of(notification));

        // Perform DELETE request to delete notification by ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/notifications/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tip_notifikacije", Matchers.is("Type")));

        // Verify that notificationsRepository.deleteById() was called with the correct ID
        verify(notificationsRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteNotification_NotFound() throws Exception {
        // Mock the behavior of NotificationsRepository
        when(notificationsRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform DELETE request with non-existing notification ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/notifications/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Nije pronadjena notifikacija sa tim ID-em.")));

        // Verify that notificationsRepository.deleteById() was not called
        verify(notificationsRepository, never()).deleteById(anyLong());
    }

    @Test
    public void updateNotification_Success() throws Exception {
        // Mock the behavior of NotificationsRepository
        Notification existingNotification = new Notification(1L, "Type", "Content", "UserUID");
        when(notificationsRepository.findById(1L)).thenReturn(Optional.of(existingNotification));
        when(notificationsRepository.save(any(Notification.class))).thenReturn(existingNotification); // mock save operation

        NotificationDTO updatedNotificationDTO = new NotificationDTO();
        updatedNotificationDTO.setTip_notifikacije("Updated Type");
        updatedNotificationDTO.setSadrzaj("Updated Content");
        updatedNotificationDTO.setUser_uid("Updated UserUID");

        mockMvc.perform(MockMvcRequestBuilders.put("/notifications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedNotificationDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tip_notifikacije").value("Updated Type"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sadrzaj").value("Updated Content"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user_uid").value("Updated UserUID"));
    }

    @Test
    public void updateNotification_NotFound() throws Exception {
        // Mock the behavior of NotificationsRepository
        when(notificationsRepository.findById(1L)).thenReturn(Optional.empty());

        // Create a NotificationDTO with updated values
        NotificationDTO updatedNotificationDTO = new NotificationDTO();
        updatedNotificationDTO.setTip_notifikacije("Updated Type");
        updatedNotificationDTO.setSadrzaj("Updated Content");
        updatedNotificationDTO.setUser_uid("Updated UserUID");

        // Perform PUT request to update the notification
        mockMvc.perform(MockMvcRequestBuilders.put("/notifications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedNotificationDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateNotification_InvalidInput_Fail() throws Exception {
        Notification existingNotification = new Notification(1L, "Type", "Content", "UserUID");
        when(notificationsRepository.findById(1L)).thenReturn(Optional.of(existingNotification));
        when(notificationsRepository.save(any(Notification.class))).thenReturn(existingNotification);

        // Create a NotificationDTO with invalid data (e.g., missing required fields)
        NotificationDTO updatedNotificationDTO = new NotificationDTO();

        // Perform PUT request to update the notification with invalid data
        mockMvc.perform(MockMvcRequestBuilders.put("/notifications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedNotificationDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void updateNotification_Failure() throws Exception {
        // Mock the behavior of NotificationsRepository
        Notification existingNotification = new Notification(1L, "Type", "Content", "UserUID");
        when(notificationsRepository.findById(1L)).thenReturn(Optional.of(existingNotification));
        when(notificationsRepository.save(any(Notification.class))).thenThrow(new RuntimeException("Failed to save notification")); // mock save failure

        // Create a NotificationDTO with updated values
        NotificationDTO updatedNotificationDTO = new NotificationDTO();
        updatedNotificationDTO.setTip_notifikacije("Updated Type");
        updatedNotificationDTO.setSadrzaj("Updated Content");
        updatedNotificationDTO.setUser_uid("Updated UserUID");

        mockMvc.perform(MockMvcRequestBuilders.put("/notifications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedNotificationDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Failed to save notification"));
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
