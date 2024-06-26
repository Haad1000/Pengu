package code.Achievements;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AchievementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenStore tokenStore;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AchievementRepository achievementRepository;

    private final String adminToken = "adminToken";
    private User adminUser;

    private Achievement achievement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // Setup admin user
        adminUser = new User(); // Adjust with actual fields
        adminUser.setRole(Roles.UserRoles.ADMIN);
        when(tokenStore.getUser(adminToken)).thenReturn(adminUser);

        // Achievement setup
        achievement = new Achievement("Initial title", "Initial description");
        achievement.setId(1); // Assume ID is generated as 1

        when(achievementRepository.findById(1)).thenReturn(achievement);
        when(achievementRepository.findAll()).thenReturn(Arrays.asList(achievement));
        when(achievementRepository.save(any(Achievement.class))).thenReturn(achievement);
    }

    @Test
    void addAchievementSuccess() throws Exception {
        mockMvc.perform(post("/addAch/{token}", adminToken)
                        .contentType("application/json")
                        .content("{\"title\":\"New Achievement\", \"desc\":\"New Description\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Achievement created successfully."));

        verify(achievementRepository, times(1)).save(any(Achievement.class));
    }

    @Test
    void updateAchievementSuccess() throws Exception {
        mockMvc.perform(put("/updateAch/{token}/{achId}", adminToken, 1)
                        .contentType("application/json")
                        .content("{\"title\":\"Updated Title\", \"desc\":\"Updated Description\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Achievement updated successfully."));

        verify(achievementRepository, times(1)).save(any(Achievement.class));
    }

    @Test
    void removeAchievementSuccess() throws Exception {
        mockMvc.perform(delete("/removeAch/{token}/{achId}", adminToken, 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Achievement removed successfully."));

        verify(achievementRepository, times(1)).delete(any(Achievement.class));
    }

    @Test
    void getAchByIdSuccess() throws Exception {
        mockMvc.perform(get("/getAchById/{achId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(achievement.getTitle()));

        verify(achievementRepository, times(1)).findById(1);
    }
}
