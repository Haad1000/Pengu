package code.TeacherSlots;

import code.TeacherCourses.Teacher;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SlotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SlotRepository slotRepository;

    @MockBean
    private TimeSlotRangeRepository timeSlotRangeRepository;

    @MockBean
    private TokenStore tokenStore;

    private User teacher = new User(); // Assumed User stub initialization for the test scenario
    private Slot slot;
    private TimeSlotRange timeSlotRange;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        slot = new Slot(LocalDate.now(), Collections.emptyList());
        timeSlotRange = new TimeSlotRange(LocalTime.of(9, 0), LocalTime.of(10, 0), true);
        // Assuming that Teacher is correctly instantiated and linked with TimeSlotRange
        Teacher teacher = new Teacher(); // Ensure you're setting up any required fields
        teacher.setId(1); // Example ID assignment
        timeSlotRange.setTeacher(teacher);

        when(tokenStore.getUser(anyString())).thenReturn(teacher);
        when(slotRepository.findById(anyInt())).thenReturn(Optional.of(slot));
        when(timeSlotRangeRepository.findById(anyLong())).thenReturn(Optional.of(timeSlotRange));
        // Ensure that the findAvailableSlotsByTeacher method is mock to return expected slots.
    }




    @Test
    void openSlotsSuccess() throws Exception {
        String slotRangesJson = "[{\"startTime\":\"09:00\",\"endTime\":\"10:00\",\"isAvailable\":true}]";

        mockMvc.perform(post("/slots/open/{token}/{date}", "validToken", LocalDate.now())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(slotRangesJson))
                .andExpect(status().isOk());

        verify(slotRepository, times(1)).save(any(Slot.class));
    }



    // Your test method adjusted:
    @Test
    void updateSlotAvailabilitySuccess() throws Exception {
        // Preparing the mock slot and timeSlotRange
        TimeSlotRange mockTimeSlotRange = new TimeSlotRange();
        mockTimeSlotRange.setId(1L);
        mockTimeSlotRange.setAvailable(false);

        Slot mockSlot = new Slot();
        mockSlot.setId(1);
        mockSlot.setAvailableSlots(List.of(mockTimeSlotRange));

        when(slotRepository.findById(any(Integer.class))).thenReturn(Optional.of(mockSlot));
        when(timeSlotRangeRepository.findById(any(Long.class))).thenReturn(Optional.of(mockTimeSlotRange));
        when(tokenStore.getUser(any(String.class))).thenReturn(new User()); // Mocking a successful token validation

        // Perform the request and capture the response for manual assertion
        MvcResult result = mockMvc.perform(put("/slots/update/{slotId}/{timeSlotRangeId}/{token}", 1, 1, "validToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Manually verify the response content
        String responseBody = result.getResponse().getContentAsString();
        assertEquals("Time slot availability updated and added to the available slots successfully.", responseBody);

        // Optionally, add verify statements for mocked methods if needed
    }




    @Test
    void getAvailableSlotsForTeacherFailure() throws Exception {
        // Simulate no available slots found for the teacher ID
        when(timeSlotRangeRepository.findAvailableSlotsByTeacher(anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/slots/available/{teacherId}", 1)) // Adjust the teacher ID as necessary
                .andExpect(status().isNoContent()); // Expecting a 204 No Content status when no slots are available
    }













}