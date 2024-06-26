package code.Biography;

import code.Banning.Banned;
import code.ProfileImages.Image;
import code.Users.TokenStore;
import code.Users.User;
import code.Users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BiographyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BiographyRepository biographyRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenStore tokenStore; // Mock the token store as well

    private static final String token = "validToken"; // example token
    private User currentUser;
    private Biography biography;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        currentUser = new User(); // Populate with actual constructor values needed
        when(tokenStore.getUser(token)).thenReturn(currentUser);

        biography = new Biography("Bio text", currentUser, 25, new Image()); // Populate with actual constructor values needed
    }

    @Test
    void testAddBioSuccessful() throws Exception {
        when(biographyRepository.save(any(Biography.class))).thenReturn(biography);

        mockMvc.perform(post("/addBio/{token}", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bio\":\"New biography\", \"age\":25}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"success\"}"));
    }

    @Test
    void testUpdateBioSuccessful() throws Exception {
        when(biographyRepository.findByUser(currentUser)).thenReturn(biography);
        when(biographyRepository.save(biography)).thenReturn(biography);

        mockMvc.perform(put("/updateBio/{token}", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bio\":\"Updated bio\", \"age\":26}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"success\"}"));

        // Verify that biography object has been updated
        verify(biographyRepository).save(biography);
    }

    @Test
    void testDeleteBioSuccessful() throws Exception {
        when(biographyRepository.findByUser(currentUser)).thenReturn(biography);

        mockMvc.perform(delete("/deleteBio/{token}", token))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"success\"}"));

        verify(biographyRepository, times(1)).deleteById(biography.getId());
    }

    @Test
    void testDeleteAllBios() throws Exception {
        when(biographyRepository.findAll()).thenReturn(Arrays.asList(new Biography(), new Biography()));

        mockMvc.perform(delete("/deleteAllBios"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(biographyRepository, times(1)).deleteAll();
    }


    // Add tests for unauthorized access scenarios here if needed
}
