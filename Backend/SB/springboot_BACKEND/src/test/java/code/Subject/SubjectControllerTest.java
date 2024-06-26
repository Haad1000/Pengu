package code.Subject;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectRepository subjectRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenStore tokenStore;

    private Subject subject;
    private User adminUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        adminUser = new User(); // Initialize User with Admin role for testing
        adminUser.setRole(Roles.UserRoles.ADMIN);
        subject = new Subject("Math");

        // Mimic the behavior of tokenStore and userRepository
        when(tokenStore.getUser(anyString())).thenReturn(adminUser);

        // Mimic the behavior of the subjectRepository
        when(subjectRepository.save(any(Subject.class))).thenReturn(subject);
        when(subjectRepository.findBySubjectID(anyInt())).thenReturn(subject);
        when(subjectRepository.findAll()).thenReturn(Collections.singletonList(subject));
    }

    @Test
    void createSubjectSuccess() throws Exception {
        mockMvc.perform(post("/createSubject/{token}", "validAdminToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"subjectName\":\"Math\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"success\"}"));

        verify(subjectRepository, times(1)).save(any(Subject.class));
    }

    @Test
    void deleteSubjectSuccess() throws Exception {
        mockMvc.perform(delete("/deleteSubject/{subjectId}/{token}", 1, "validAdminToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"success\"}"));

        verify(subjectRepository, times(1)).deleteSubjectBySubjectID(anyInt());
    }

    @Test
    void updateSubjectSuccess() throws Exception {
        mockMvc.perform(put("/updateSubject/{token}", "validAdminToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"subjectID\":1, \"subjectName\":\"Physics\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"success\"}"));

        verify(subjectRepository, times(1)).save(any(Subject.class));
    }

    @Test
    void printSubjectSuccess() throws Exception {
        mockMvc.perform(get("/printSubject"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"subjectID\":0,\"subjectName\":\"Math\"}]"));

        verify(subjectRepository, times(1)).findAll();
    }

    @Test
    void deleteAllSubjectsSuccess() throws Exception {
        mockMvc.perform(delete("/deleteAllSubjects"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(subjectRepository, times(1)).deleteAll();
    }


}