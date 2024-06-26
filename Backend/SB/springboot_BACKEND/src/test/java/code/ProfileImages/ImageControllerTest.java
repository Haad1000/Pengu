package code.ProfileImages;

import code.Biography.Biography;
import code.Biography.BiographyRepository;
import code.Users.TokenStore;
import code.Users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageRepository imageRepository;

    @MockBean
    private TokenStore tokenStore;

    @MockBean
    private BiographyRepository biographyRepository;

    private User user;
    private Biography bio;
    private Image image;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        user = new User(); // Initialize User
        bio = new Biography(); // Initialize Biography
        image = new Image("file/path/name.jpg", user); // Initialize Image

        when(tokenStore.getUser(anyString())).thenReturn(user);
        when(biographyRepository.findByUserId(anyInt())).thenReturn(bio);
        when(imageRepository.save(any(Image.class))).thenReturn(image);
    }



    @Test
    void deleteImageSuccess() throws Exception {
        bio.setImage(image);

        mockMvc.perform(delete("/images/deleteImage/{token}", "validToken"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Image deleted successfully")));

        verify(imageRepository, times(1)).delete(any(Image.class));
    }

    @Test
    void deleteAllImagesSuccess() throws Exception {
        when(imageRepository.findAll()).thenReturn(Arrays.asList(new Image(), new Image()));
        mockMvc.perform(delete("/deleteAllImages"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(imageRepository, times(1)).deleteAll();
    }


}