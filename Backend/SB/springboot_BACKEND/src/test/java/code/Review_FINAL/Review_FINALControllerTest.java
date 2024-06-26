package code.Review_FINAL;

import code.ProfileImages.Image;
import code.Users.TokenStore;
import code.Users.User;
import code.Users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static java.util.function.Predicate.not;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class Review_FINALControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenStore tokenStore;

    private User user;
    private User teacher;
    private Review review;

    @BeforeEach
    public void setUp() {
        user = new User();
        teacher = new User();
        review = new Review(user, teacher, "Great teacher");

        // Mimic the behavior of tokenStore and userRepository
        when(tokenStore.getUser(anyString())).thenReturn(user);
        when(userRepository.findById(anyInt())).thenReturn(teacher);
        // Mimic the behavior of the reviewRepository
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewRepository.findReviewByWrittenById(anyInt())).thenReturn(review);
        when(reviewRepository.findReviewByID(anyInt())).thenReturn(review);
    }

    @Test
    void createReviewSuccess() throws Exception {
        mockMvc.perform(post("/createReview/{token}", "validToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"writtenFor\": {\"id\": 1}, \"actualReview\": \"Great teacher\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"success\"}"));

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void updateReviewSuccess() throws Exception {
        mockMvc.perform(put("/updateReview/{token}", "validToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"actualReview\": \"Excellent teacher\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"success\"}"));

        verify(reviewRepository, times(1)).save(any(Review.class));
    }


    @Test
    void deleteReviewSuccess() throws Exception {
        mockMvc.perform(delete("/deleteReview/{token}", "validToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"success\"}"));

        verify(reviewRepository, times(1)).delete(any(Review.class));
    }

    @Test
    void deleteAllReviewsSuccess() throws Exception {
        when(reviewRepository.findAll()).thenReturn(Arrays.asList(new Review(), new Review()));
        mockMvc.perform(delete("/deleteAllReviews"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(reviewRepository, times(1)).deleteAll();
    }
}