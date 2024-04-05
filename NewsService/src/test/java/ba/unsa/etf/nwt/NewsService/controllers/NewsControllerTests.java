package ba.unsa.etf.nwt.NewsService.controllers;

import ba.unsa.etf.nwt.NewsService.DTO.NewsDTO;
import ba.unsa.etf.nwt.NewsService.model.ErrorMsg;
import ba.unsa.etf.nwt.NewsService.model.News;
import ba.unsa.etf.nwt.NewsService.repositories.NewsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NewsControllerTests {

    @MockBean
    private NewsRepository newsRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    public void getNews_Success() throws Exception {
        // Mock the behavior of NewsRepository
        when(newsRepository.findAll()).thenReturn(List.of(new News(1L, "Title", "Content", "Image", "UserUID")));

        // Perform GET request to fetch all news
        mockMvc.perform(MockMvcRequestBuilders.get("/news"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].naslov", Matchers.is("Title")));
    }

    @Test
    public void getNews_EmptyList_Success() throws Exception {
        // Mock the behavior of NewsRepository
        when(newsRepository.findAll()).thenReturn(Collections.emptyList());

        // Perform GET request when no news are found
        mockMvc.perform(MockMvcRequestBuilders.get("/news"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(0)));
    }

    @Test
    public void createNews_Success() throws Exception {
        // Mock the behavior of NewsRepository
        when(newsRepository.save(any(News.class))).thenReturn(new News(1L, "Title", "Content", "Image", "UserUID"));

        // Create a news DTO for the request body
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setNaslov("Title");
        newsDTO.setSadrzaj("Content");
        newsDTO.setSlika("Image");
        newsDTO.setUser_uid("UserUID");

        // Perform POST request to create a new news
        mockMvc.perform(post("/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newsDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.naslov", Matchers.is("Title")));

        // Verify that newsRepository.save() was called with the correct News object
        verify(newsRepository, times(1)).save(any(News.class));
    }

    @Test
    public void createNews_InvalidInput_Fail() throws Exception {
        // Prepare invalid news data
        NewsDTO newsDTO = new NewsDTO();

        // Perform the POST request
        MvcResult result = mockMvc.perform(post("/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newsDTO)))
                .andExpect(status().isForbidden()) // Expecting 403
                .andReturn();

        // Extract the response body as a String
        String responseBody = result.getResponse().getContentAsString();

        // Assert the response body contains the expected error messages
        assertThat(responseBody).contains("SadrÅ¾aj novosti je obavezan");
        assertThat(responseBody).contains("Slika je obavezna");
        assertThat(responseBody).contains("UID korisnika je obavezan");
        assertThat(responseBody).contains("Naslov je obavezan");
    }
    @Test
    public void getNewsByID_Success() throws Exception {
        // Mock the behavior of NewsRepository
        News news = new News(1L, "Title", "Content", "Image", "UserUID");
        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));

        // Perform GET request to fetch news by ID
        mockMvc.perform(MockMvcRequestBuilders.get("/news/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.naslov", Matchers.is("Title")));
    }

    @Test
    public void getNewsByID_NotFound() throws Exception {
        // Mock the behavior of NewsRepository
        when(newsRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform GET request with non-existing news ID
        mockMvc.perform(MockMvcRequestBuilders.get("/news/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Nije pronadjena novost sa tim ID-em.")));

    }

    @Test
    public void deleteNews_Success() throws Exception {
        // Mock the behavior of NewsRepository
        News news = new News(1L, "Title", "Content", "Image", "UserUID");
        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));

        // Perform DELETE request to delete news by ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/news/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.naslov", Matchers.is("Title")));

        // Verify that newsRepository.deleteById() was called with the correct ID
        verify(newsRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteNews_NotFound() throws Exception {
        // Mock the behavior of NewsRepository
        when(newsRepository.findById(1L)).thenReturn(Optional.empty());

        // Perform DELETE request with non-existing news ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/news/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("validation")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Nije pronadjena novost sa tim ID-em.")));


        // Verify that newsRepository.deleteById() was not called
        verify(newsRepository, never()).deleteById(anyLong());
    }

    @Test
    public void updateNews_Success() throws Exception {
        // Mock the behavior of NewsRepository
        News existingNews = new News(1L, "Title", "Content", "Image", "UserUID");
        when(newsRepository.findById(1L)).thenReturn(Optional.of(existingNews));
        when(newsRepository.save(any(News.class))).thenReturn(existingNews); // mock save operation

        NewsDTO updatedNewsDTO = new NewsDTO();
        updatedNewsDTO.setNaslov("Updated Title");
        updatedNewsDTO.setSadrzaj("Updated Content");
        updatedNewsDTO.setSlika("Updated Image");
        updatedNewsDTO.setUser_uid("Updated UserUID");

        mockMvc.perform(MockMvcRequestBuilders.put("/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedNewsDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.naslov").value("Updated Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sadrzaj").value("Updated Content"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.slika").value("Updated Image"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user_uid").value("Updated UserUID"));
    }

    @Test
    public void updateNews_NotFound() throws Exception {
        // Mock the behavior of NewsRepository
        when(newsRepository.findById(1L)).thenReturn(Optional.empty());

        // Create a NewsDTO with updated values
        NewsDTO updatedNewsDTO = new NewsDTO();
        updatedNewsDTO.setNaslov("Updated Title");
        updatedNewsDTO.setSadrzaj("Updated Content");
        updatedNewsDTO.setSlika("Updated Image");
        updatedNewsDTO.setUser_uid("Updated UserUID");

        // Perform PUT request to update the news
        mockMvc.perform(MockMvcRequestBuilders.put("/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedNewsDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateNews_InvalidInput_Fail() throws Exception {
        News existingNews = new News(1L, "Title", "Content", "Image", "UserUID");
        when(newsRepository.findById(1L)).thenReturn(Optional.of(existingNews));
        when(newsRepository.save(any(News.class))).thenReturn(existingNews);

        // Create a NewsDTO with invalid data (e.g., missing required fields)
        NewsDTO updatedNewsDTO = new NewsDTO();

        // Perform PUT request to update the news with invalid data
        mockMvc.perform(MockMvcRequestBuilders.put("/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedNewsDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void updateNews_Failure() throws Exception {
        // Mock the behavior of NewsRepository
        News existingNews = new News(1L, "Title", "Content", "Image", "UserUID");
        when(newsRepository.findById(1L)).thenReturn(Optional.of(existingNews));
        when(newsRepository.save(any(News.class))).thenThrow(new RuntimeException("Failed to save news")); // mock save failure

        // Create a NewsDTO with updated values
        NewsDTO updatedNewsDTO = new NewsDTO();
        updatedNewsDTO.setNaslov("Updated Title");
        updatedNewsDTO.setSadrzaj("Updated Content");
        updatedNewsDTO.setSlika("Updated Image");
        updatedNewsDTO.setUser_uid("Updated UserUID");

        mockMvc.perform(MockMvcRequestBuilders.put("/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedNewsDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Failed to save news"));
    }

//    @Test
//    public void updateNaslov_Success() throws Exception {
//        // Mock the behavior of NewsRepository
//        News news = new News(1L, "Title", "Content", "Image", "UserUID");
//        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));
//
//        // Perform PATCH request to update title of news
//        mockMvc.perform(MockMvcRequestBuilders.patch("/news/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("New Title"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.naslov", Matchers.is("New Title")));
//
//        // Verify that newsRepository.save() was called with the correct News object
//        verify(newsRepository, times(1)).save(any(News.class));
//    }
//
//
//    @Test
//    public void updateNaslov_NotFound() throws Exception {
//        // Mock the behavior of NewsRepository
//        when(newsRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Perform PUT request with non-existing news ID
//        mockMvc.perform(MockMvcRequestBuilders.patch("/news/1/naslov/New Title")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Nije pronadjena novost sa tim ID-em.")));
//
//        // Verify that newsRepository.save() was not called
//        verify(newsRepository, never()).save(any(News.class));
//    }
//
//    @Test
//    public void updateSadrzaj_Success() throws Exception {
//        // Mock the behavior of NewsRepository
//        News news = new News(1L, "Title", "Content", "Image", "UserUID");
//        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));
//
//        // Perform PUT request to update content of news
//        mockMvc.perform(MockMvcRequestBuilders.patch("/news/1/sadrzaj/New Content")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.sadrzaj", Matchers.is("New Content")));
//
//        // Verify that newsRepository.save() was called with the correct News object
//        verify(newsRepository, times(1)).save(any(News.class));
//    }
//
//    @Test
//    public void updateSadrzaj_NotFound() throws Exception {
//        // Mock the behavior of NewsRepository
//        when(newsRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Perform PUT request with non-existing news ID
//        mockMvc.perform(MockMvcRequestBuilders.patch("/news/1/sadrzaj/New Content")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Nije pronadjena novost sa tim ID-em.")));
//
//        // Verify that newsRepository.save() was not called
//        verify(newsRepository, never()).save(any(News.class));
//    }
//
//    @Test
//    public void updateSlika_Success() throws Exception {
//        // Mock the behavior of NewsRepository
//        News news = new News(1L, "Title", "Content", "Image", "UserUID");
//        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));
//
//        // Perform PUT request to update image of news
//        mockMvc.perform(MockMvcRequestBuilders.patch("/news/1/slika/New Image")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.slika", Matchers.is("New Image")));
//
//        // Verify that newsRepository.save() was called with the correct News object
//        verify(newsRepository, times(1)).save(any(News.class));
//    }
//
//    @Test
//    public void updateSlika_NotFound() throws Exception {
//        // Mock the behavior of NewsRepository
//        when(newsRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Perform PUT request with non-existing news ID
//        mockMvc.perform(MockMvcRequestBuilders.patch("/news/1/slika/New Image")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Nije pronadjena novost sa tim ID-em.")));
//
//        // Verify that newsRepository.save() was not called
//        verify(newsRepository, never()).save(any(News.class));
//    }
//
//    @Test
//    public void updateUserID_Success() throws Exception {
//        // Mock the behavior of NewsRepository
//        News news = new News(1L, "Title", "Content", "Image", "UserUID");
//        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));
//
//        // Perform PUT request to update user ID of news
//        mockMvc.perform(MockMvcRequestBuilders.patch("/news/1/user/UserID")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.user_uid", Matchers.is("UserID")));
//
//        // Verify that newsRepository.save() was called with the correct News object
//        verify(newsRepository, times(1)).save(any(News.class));
//    }
//
//    @Test
//    public void updateUserID_NotFound() throws Exception {
//        // Mock the behavior of NewsRepository
//        when(newsRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Perform PUT request with non-existing news ID
//        mockMvc.perform(MockMvcRequestBuilders.patch("/news/1/user/UserID")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Nije pronadjena novost sa tim ID-em.")));
//
//        // Verify that newsRepository.save() was not called
//        verify(newsRepository, never()).save(any(News.class));
//    }

    // Utility method to convert object to JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
