package code.Achievements;


import code.Users.Roles;
import code.Users.TokenStore;
import code.Users.User;
import code.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AchievementController {

    @Autowired
    TokenStore tokenStore;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AchievementRepository achievementRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    // Placeholder methods

    @PostMapping("/addAch/{token}")
    public ResponseEntity<String> addAchievement(@PathVariable("token") String token, @RequestBody AchievementRequest achievementRequest){
        User admin = tokenStore.getUser(token);
        // Null check for the 'User' object
    if (admin == null) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Invalid token.");
    }
    if (!admin.getRole().equals(Roles.UserRoles.ADMIN)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
    }
        if(achievementRequest.getTitle() == null || achievementRequest.getDesc() == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot create an empty achievement.");
        }
        Achievement newAchievement = new Achievement(achievementRequest.getTitle(), achievementRequest.getDesc());
        achievementRepository.save(newAchievement);
        return ResponseEntity.ok("Achievement created successfully.");
    }

    @PutMapping("/updateAch/{token}/{achId}")
    public ResponseEntity<String> updateAchievement(@PathVariable("token") String token, @PathVariable("achId") int achId, @RequestBody AchievementRequest achievementRequest) {
        User admin = tokenStore.getUser(token);

        // Null check for the 'User' object
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Invalid token.");
        }

        if (!admin.getRole().equals(Roles.UserRoles.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }

        Achievement existingAchievement = achievementRepository.findById(achId);

        if (existingAchievement == null) {
            return ResponseEntity.badRequest().body("Achievement not found.");
        }

        if (achievementRequest.getTitle() == null || achievementRequest.getDesc() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot update an achievement with missing fields.");
        }

        existingAchievement.setTitle(achievementRequest.getTitle());
        existingAchievement.setDesc(achievementRequest.getDesc());

        achievementRepository.save(existingAchievement);

        return ResponseEntity.ok("Achievement updated successfully.");
    }

    @DeleteMapping("/removeAch/{token}/{achId}")
    public ResponseEntity<String> removeAchievement(@PathVariable("token") String token, @PathVariable("achId") int achId){
        User admin = tokenStore.getUser(token);
        // Null check for the 'User' object
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Invalid token.");
        }
        if (!admin.getRole().equals(Roles.UserRoles.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }
        Achievement achievement = achievementRepository.findById(achId);
        if (achievement == null){
            return ResponseEntity.badRequest().body("Achievement not found.");
        }
        achievementRepository.delete(achievement);
        return ResponseEntity.ok("Achievement removed successfully.");
    }

    @GetMapping("/getAllAch")
    public ResponseEntity<?> getAchievements() {
        List<Achievement> achievementList = achievementRepository.findAll();
        return ResponseEntity.ok(achievementList);
    }

    @GetMapping("/getAchById/{achId}")
    public Achievement getAchById(@PathVariable("achId") int achId){
        return achievementRepository.findById(achId);
    }

}
