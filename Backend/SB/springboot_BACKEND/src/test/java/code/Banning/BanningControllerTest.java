package code.Banning;

import code.Users.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BanningControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BannedRepository bannedRepository;

    @MockBean
    private TokenStore tokenStore; // Mock the token store as well

    private final String adminToken = "adminToken";
    private User adminUser;
    private User normalUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // Setup admin user
        adminUser = new User(); // Please adjust with actual fields
        adminUser.setRole(Roles.UserRoles.ADMIN);
        when(tokenStore.getUser(adminToken)).thenReturn(adminUser);

        // Setup a normal user to be banned/unbanned
        normalUser = new User(); // Please adjust with actual fields
        normalUser.setEmailId("normalUser@example.com");

        // Setup repository behavior
        when(userRepository.findByEmailId("normalUser@example.com")).thenReturn(normalUser);

        // Assume no banned user initially
        when(bannedRepository.findByEmailId("normalUser@example.com")).thenReturn(null);
    }

    @Test
    void banUserSuccess() throws Exception {
        mockMvc.perform(post("/banUser/{token}/{emailId}", adminToken, "normalUser@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("User banned successfully"));

        // Verify that repository methods were called
        verify(bannedRepository, times(1)).save(any(Banned.class));
    }

    @Test
    void unbanUserSuccess() throws Exception {
        // Setup a banned user before unbanning
        when(bannedRepository.findByEmailId("normalUser@example.com")).thenReturn(new Banned());

        mockMvc.perform(post("/unbanUser/{token}/{emailId}", adminToken, "normalUser@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("User unbanned successfully"));

        // Verify that repository methods were called
        verify(bannedRepository, times(1)).delete(any(Banned.class));
    }

    @Test
    void getAllBannedUsersSuccess() throws Exception {
        List<Banned> bannedList = Arrays.asList(new Banned(), new Banned()); // Assumed having some banned users
        when(bannedRepository.findAll()).thenReturn(bannedList);

        mockMvc.perform(get("/getAllBannedUsers/{token}", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(bannedList.size()));
    }

    @Test
    void deleteAllBannedSuccess() throws Exception {
        // Given the repository is not empty
        when(bannedRepository.findAll()).thenReturn(Arrays.asList(new Banned(), new Banned()));

        // When deleting all
        mockMvc.perform(delete("/deleteAllBanned"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(bannedRepository, times(1)).deleteAll();
    }

    // Additional test cases could include testing access control (e.g., ensuring a non-admin can't ban/unban),
    // attempting to ban/unban a user that doesn't exist, or attempting to ban a user who is already banned.
}