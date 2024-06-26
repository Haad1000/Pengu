package code.Rating;

import code.Users.Roles;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingRepository ratingRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenStore tokenStore;

    private User student;
    private User teacher;
    private Rating rating;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // Setup mock user (student and teacher)
        student = new User(); // Adjust with actual fields
        teacher = new User(); // Adjust with actual fields
        teacher.setId(1); // Providing ID to the teacher

        // Mock the userRepository
        when(userRepository.findById(teacher.getId())).thenReturn(teacher);

        // Setup mock rating
        rating = new Rating(student, teacher, 5, 4, 5);
        rating.setId(1); // Providing ID to the rating

        // Mocking the tokenStore to return the student
        when(tokenStore.getUser(anyString())).thenReturn(student);

        // Mocking the ratingRepository
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
        when(ratingRepository.findAllRatingByTeacherId(teacher.getId())).thenReturn(Arrays.asList(rating));
        when(ratingRepository.findById(rating.getId())).thenReturn(rating);
        when(ratingRepository.findAll()).thenReturn(Arrays.asList(rating));
        when(ratingRepository.findAllRatingByStudentId(student.getId())).thenReturn(Arrays.asList(rating));
    }

    @Test
    void rateSuccess() throws Exception {
        mockMvc.perform(post("/rate/{token}", "validToken")
                        .contentType("application/json")
                        .content("{\"teacherId\":\"1\", \"communicationRating\":\"5\", \"teachingRating\":\"4\", \"recommendRating\":\"5\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"success\"}")); // Updated expected content

        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    void getTeacherRatingsSuccess() throws Exception {
        mockMvc.perform(get("/getTeacherRatings/{teacherId}", teacher.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].overall_rating").value(rating.getOverall_rating()));

        verify(ratingRepository, times(1)).findAllRatingByTeacherId(teacher.getId());
    }

    @Test
    void updateRatingSuccess() throws Exception {
        mockMvc.perform(put("/updateRating/{ratingId}", rating.getId())
                        .contentType("application/json")
                        .content("{\"communicationRating\":\"5\", \"teachingRating\":\"5\", \"recommendRating\":\"5\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"success\"}")); // Updated expected content

        verify(ratingRepository, times(1)).save(rating);
    }

    @Test
    void deleteAllRatingsSuccess() throws Exception {
        mockMvc.perform(delete("/deleteAllRatings"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(ratingRepository, times(1)).deleteAll();
    }

    // More test cases can be added to cover additional methods and failure scenarios
}