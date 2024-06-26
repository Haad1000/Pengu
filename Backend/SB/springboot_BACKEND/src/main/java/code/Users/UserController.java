package code.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import code.Banning.Banned;
import code.Banning.BannedRepository;
import code.TeacherCourses.Teacher;
import code.Users.TokenUtility;
import code.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;


/**
 *
 * @author Vivek Bengre
 *
 */

@RestController
public class UserController {

    @Autowired
    BannedRepository bannedRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TokenStore tokenStore;



    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";
    public User currentUser;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        List<User> userList = userRepository.findAll();
        List<Banned> bList = bannedRepository.findAll();
        for (User user : userList) {
            Banned banned = bannedRepository.findByEmailId(loginRequest.getEmailId());
            if(user.getIsActive().equals(IsActive.ACTIVE))
                if (loginRequest.getEmailId().equals(user.getEmailId()) && loginRequest.getPassword().equals(user.getPassword())) {
                    String token = TokenUtility.generateNewToken();
                    tokenStore.storeUser(token, user);

                    // Assuming user.getRole() returns the role of the user.
                    Roles.UserRoles userRole = user.getRole();

                    int userId = user.getId();

                    // Including the role in the response along with the token.
                    return ResponseEntity.ok().body(Map.of("token", token, "role", userRole, "userId", userId));
                }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Login failed"));
    }


    @PostMapping("/updateAcc/{token}")
    public ResponseEntity<?> updateAcc(@RequestBody RegisterRequest registerRequest, @PathVariable(name ="token") String token) {
        User user = tokenStore.getUser(token);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(failure);
        }
        User editUser = userRepository.findById(user.getId());
        if (editUser != null) {
            editUser.setName(registerRequest.getName());
            editUser.setEmailId(registerRequest.getEmailId());
            editUser.setPassword(registerRequest.getPassword());
            editUser.setRole(registerRequest.getRole());
            userRepository.save(editUser);
            return ResponseEntity.ok().body(success);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failure);
    }




    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value="Authorization") String token) {
        tokenStore.removeUser(token);
        return ResponseEntity.ok(success);
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registrationRequest) {
        if (registrationRequest.getName() == null || registrationRequest.getEmailId() == null || registrationRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body("Registration details are incomplete.");
        }

        // Check if the email already exists in the database
        User emailExists = userRepository.findByEmailId(registrationRequest.getEmailId());
        if (emailExists != null) {
            return ResponseEntity.badRequest().body("Email already registered. Please use a different email.");
        }

        User newUser;

        if (registrationRequest.getRole() == Roles.UserRoles.TEACHER) {
            // If the role is TEACHER, instantiate a Teacher object with an empty list for teacherCourses
            newUser = new Teacher(
                    registrationRequest.getName(),
                    registrationRequest.getEmailId(),
                    registrationRequest.getPassword(),
                    registrationRequest.getRole(),
                    registrationRequest.getIsActive(),
                    new ArrayList<>() // Initialize with an empty list for teacherCourses
            );
        } else {
            // For other roles, instantiate a User object
            newUser = new User(
                    registrationRequest.getName(),
                    registrationRequest.getEmailId(),
                    registrationRequest.getPassword(),
                    registrationRequest.getRole(),
                    registrationRequest.getIsActive()
            );
        }
        userRepository.save(newUser); // This will save either a User or a Teacher, depending on the role
        return ResponseEntity.ok(success);
    }



    @GetMapping("/account/{token}")
    public User account(@PathVariable(value="token", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        User currentUser = tokenStore.getUser(token);
        if (currentUser == null) {
            return null;
        }
        return currentUser;
    }



    @DeleteMapping(path = "/deleteAllUsers")
    String deleteAllUsers() {
        List<User> UserList = userRepository.findAll();
        for (User user : UserList) {
            userRepository.delete(user);
        }
        return  success;
    }

    @DeleteMapping(path = "/delete/{token}")
    ResponseEntity<?> deleteUser(@PathVariable(value="token") String token){
        User user = tokenStore.getUser(token);
        if (user != null) {
            userRepository.deleteById(user.getId());
            return ResponseEntity.ok(success);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(failure);
    }

    @GetMapping(path = "/getUser/{token}")
    ResponseEntity<?> getCurrentUser123(@PathVariable(value="token") String token){
        User retUser = tokenStore.getUser(token);
        if (retUser != null) {
            return ResponseEntity.ok(retUser);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(failure);
    }

    @GetMapping(path = "/getUserByID/{userId}")
    User getUserById(@PathVariable("userId") int userId){
        return userRepository.findById(userId);   
    }



    @GetMapping(path = "getAllUsers")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

}
