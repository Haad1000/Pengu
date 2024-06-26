package code.Biography;

import code.ProfileImages.Image;
import code.Users.TokenStore;
import code.Users.User;

import code.Users.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

@RestController
public class BiographyController {

    @Autowired
    private BiographyRepository biographyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenStore tokenStore; // Assuming you have this service


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @PostMapping("/addBio/{token}")
    public ResponseEntity<String> addBio(@PathVariable String token, @RequestBody BiographyRequest biographyRequest) {
        User currentUser = tokenStore.getUser(token);
        if (currentUser != null) {
            Biography info = new Biography(biographyRequest.getBio(), currentUser,  biographyRequest.getAge(), new Image());
            biographyRepository.save(info);
            return ResponseEntity.ok(success);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(failure);
    }

    @PutMapping("/updateBio/{token}")
    public ResponseEntity<String> updateBio(@PathVariable String token, @RequestBody BiographyRequest biographyRequest) {
        User currentUser = tokenStore.getUser(token);
        if (currentUser != null) {
            Biography info = biographyRepository.findByUser(currentUser);
            if (info != null) {
                info.setBio(biographyRequest.getBio());
                biographyRepository.save(info);
                return ResponseEntity.ok(success);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failure);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(failure);
    }

    @DeleteMapping("/deleteBio/{token}")
    public ResponseEntity<String> deleteBio(@PathVariable String token) {
        User currentUser = tokenStore.getUser(token);
        if (currentUser != null) {
            Biography info = biographyRepository.findByUser(currentUser);
            if (info != null) {
                biographyRepository.deleteById(info.getId());
                return ResponseEntity.ok(success);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failure);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(failure);
    }

    @GetMapping("/getBio/{userId}")
    public ResponseEntity<String> getBio(@PathVariable("userId") int userId){
        User currentUser = userRepository.findById(userId);
        if (currentUser != null) {
            Biography info = biographyRepository.findByUser(currentUser);
            return ResponseEntity.ok(info != null ? info.getBio() : "");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(failure);
    }

    @DeleteMapping("/deleteAllBios")
    public String deleteAllBios(){
        if(!biographyRepository.findAll().isEmpty()){
            biographyRepository.deleteAll();
            return "success";
        }
        return "failure";
    }

}
