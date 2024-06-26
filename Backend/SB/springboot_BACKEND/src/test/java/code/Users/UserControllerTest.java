package code.Users;

import code.Banning.Banned;
import code.Users.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import code.Banning.BannedRepository;
import code.Users.Roles.UserRoles;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BannedRepository bannedRepository;

    @MockBean
    private TokenStore tokenStore;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        User validUser = new User("John Doe", "john@example.com", "securePass123", UserRoles.STUDENT, IsActive.ACTIVE);
        when(userRepository.findByEmailId("john@example.com")).thenReturn(validUser);

//        Banned bannedUser = new Banned(validUser.getName(), validUser.getEmailId(), validUser.getPassword(), validUser.getRole());
//        when(bannedRepository.findByEmailId("john@example.com")).thenReturn(bannedUser);
    }


    @Test
    void testLogin2() throws Exception {
        when(userRepository.findByEmailId("wrong@example.com")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"emailId\": \"emre@email.com\", \"password\": \"wrongpass\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRegister1() throws Exception {
        User newUser = new User("John Doe", "john.doe@example.com", "password123", UserRoles.STUDENT, IsActive.ACTIVE);

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\", \"emailId\": \"john.doe@example.com\", \"password\": \"password123\", \"role\": \"STUDENT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    void testRegister2() throws Exception {
        User newUser = new User("John Doe", "john.doe@example.com", "password123", null, IsActive.ACTIVE);

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\", \"john.doe@example.com\", \"password\": \"password123\", \"role\": \"DEAN\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(new User("user1", "user1@example.com", "password1", UserRoles.STUDENT, IsActive.ACTIVE));
        users.add(new User("user2", "user2@example.com", "password2", UserRoles.TEACHER, IsActive.ACTIVE));

        when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/getAllUsers"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("user1"));
    }
}