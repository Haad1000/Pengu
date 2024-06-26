package code.Booking;

import code.Subject.Subject;
import code.Subject.SubjectRepository;
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
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SubjectRepository subjectRepository;

    private User teacher;
    private User student;
    private Subject subject;
    private Booking booking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        teacher = new User(); // Initialize Teacher
        student = new User(); // Initialize Student
        subject = new Subject(); // Initialize Subject
        booking = new Booking(); // Initialize Booking

        when(userRepository.findById(anyInt())).thenReturn(teacher);
        when(subjectRepository.findById(anyInt())).thenReturn(Optional.ofNullable(subject));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
    }


    @Test
    void printBookingInfoSuccess() throws Exception {
        when(bookingRepository.findAll()).thenReturn(Collections.singletonList(booking));

        mockMvc.perform(get("/printBookingInfo"))
                .andExpect(status().isOk());

        verify(bookingRepository, times(1)).findAll();
    }


// Other imports remain the same



    @Test
    void deleteBookingSuccess() throws Exception {
        when(bookingRepository.findBookingByID(anyInt())).thenReturn(Optional.ofNullable(booking));

        mockMvc.perform(delete("/deleteBooking/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"success\"}"));

        verify(bookingRepository, times(1)).delete(any(Booking.class));
    }

    @Test
    void deleteAllBookingSuccess() throws Exception {
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(new Booking(), new Booking()));
        mockMvc.perform(delete("/deleteAllBooking"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(bookingRepository, times(1)).deleteAll();
    }





    @Test
    void deleteBookingFailure() throws Exception {
        int nonExistentId = 999; // Assuming ID 999 does not exist
        when(bookingRepository.findBookingByID(nonExistentId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/deleteBooking/{id}", nonExistentId))
                .andExpect(status().isOk()) // Assuming your controller returns 200 OK with a failure message
                .andExpect(content().string("{\"message\":\"failure\"}")); // Assuming "failure" is the indicator of the operation not succeeding
    }

    @Test
    void deleteAllBookingFailure() throws Exception {
        // Simulate the repository operation throwing an exception
        doThrow(new RuntimeException("Database error")).when(bookingRepository).deleteAll();

        // Perform request and expect a 200 status with a "failure" body,
        // indicating that the system gracefully handles the failure.
        mockMvc.perform(delete("/deleteAllBooking"))
                .andExpect(status().isOk()) // Reflecting the actual behavior
                .andExpect(content().string("failure")); // Verify the response contains the "failure" message
    }
}