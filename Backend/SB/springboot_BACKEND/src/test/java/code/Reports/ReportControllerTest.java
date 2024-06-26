package code.Reports;

import code.Users.*;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportRepository reportRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenStore tokenStore;

    private User student;
    private User teacher;
    private Report report;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        student = new User(); // Assume there's a constructor to set properties
        teacher = new User(); // Assume there's a constructor to set properties
        teacher.setId(1); // Simulate database ID for teacher

        when(tokenStore.getUser(anyString())).thenReturn(student);
        when(userRepository.findById(teacher.getId())).thenReturn(teacher);

        report = new Report(student, teacher, "Dissatisfaction", "Issue in course content", ReportStatus.ONGOING);
        when(reportRepository.save(any(Report.class))).thenReturn(report);
        when(reportRepository.findReportById(report.getId())).thenReturn(report);
        when(reportRepository.findAll()).thenReturn(Arrays.asList(report));
        when(reportRepository.findReportsByStudentReporting(student)).thenReturn(Arrays.asList(report));
    }

    @Test
    void createReportSuccess() throws Exception {
        mockMvc.perform(post("/createReport/{token}", "validStudentToken")
                        .contentType("application/json")
                        .content("{\"teacherReportedID\":\"1\", \"title\":\"Dissatisfaction\", \"description\":\"Issue in course content\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"success\"}"));

        verify(reportRepository, times(1)).save(any(Report.class));
    }

    @Test
    void updateReportSuccess() throws Exception {
        mockMvc.perform(put("/updateReport/{id}", report.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"success\"}"));

        verify(reportRepository, times(1)).save(report);
    }

    @Test
    void deleteAllReportsUnauthorized() throws Exception {
        // Simulate unauthorized token usage
        student = new User();
        student.setRole(Roles.UserRoles.STUDENT);
        when(tokenStore.getUser(anyString())).thenReturn(student); // Assuming student is NOT an admin
        mockMvc.perform(delete("/deleteAllReports/{token}", "unauthorizedToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"failure\"}"));
    }

    @Test
    void deleteAllReportsAuthorized() throws Exception {
        // Simulate authorized token usage by an ADMIN
        student = new User();
        student.setRole(Roles.UserRoles.ADMIN);
        when(tokenStore.getUser(anyString())).thenReturn(student); // Assuming student is NOT an admin
        mockMvc.perform(delete("/deleteAllReports/{token}", "authorizedToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"success\"}"));
    }
    

    @Test
    void getAllReportsSuccess() throws Exception {
        mockMvc.perform(get("/getAllReports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(report.getTitle()));

        verify(reportRepository, times(1)).findAll();
    }
}