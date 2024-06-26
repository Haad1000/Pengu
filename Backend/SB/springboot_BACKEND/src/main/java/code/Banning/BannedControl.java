package code.Banning;


import code.Users.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BannedControl {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BannedRepository bannedRepository;

    @Autowired
    private TokenStore tokenStore;



    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @PostMapping("/banUser/{token}/{emailId}")
    public ResponseEntity<String> banUser(@PathVariable("token") String token, @PathVariable("emailId") String emailId){
        User admin = tokenStore.getUser(token);
        if (!admin.getRole().equals(Roles.UserRoles.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        User user = userRepository.findByEmailId(emailId);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Banned banned = bannedRepository.findByEmailId(emailId);
        if (banned != null) {
            return ResponseEntity.badRequest().body("User already banned");
        }

        user.setIsActive(IsActive.INACTIVE);
        banned = new Banned(user.getName(), user.getEmailId(), user.getPassword(), user.getRole());
        bannedRepository.save(banned);
        return ResponseEntity.ok("User banned successfully");
    }

    @PostMapping("/unbanUser/{token}/{emailId}")
    public ResponseEntity<String> unbanUser(@PathVariable("token") String token, @PathVariable("emailId") String emailId){
        User admin = tokenStore.getUser(token);
        if (!admin.getRole().equals(Roles.UserRoles.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        User user = userRepository.findByEmailId(emailId);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Banned banned = bannedRepository.findByEmailId(emailId);
        if (banned == null) {
            return ResponseEntity.badRequest().body("User not currently banned");
        }

        user.setIsActive(IsActive.ACTIVE);
        bannedRepository.delete(banned);
        return ResponseEntity.ok("User unbanned successfully");
    }

    @GetMapping("/getAllBannedUsers/{token}")
    public List<Banned> getAllBannedUsers(@PathVariable("token") String token){
        List<Banned> bList = bannedRepository.findAll();
        return bList;
    }

    @DeleteMapping("/deleteAllBanned")
    public String deleteAllBanned(){
        if(!bannedRepository.findAll().isEmpty()){
            bannedRepository.deleteAll();
            return "success";
        }
        return "failure";
    }

}
