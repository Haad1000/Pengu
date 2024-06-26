package code.Rating;

import code.Users.*;
import code.Users.TokenStore;
import code.Users.User;
import code.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RatingController {

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TokenStore tokenStore;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @PostMapping("/rate/{token}")
    public String rate(@RequestBody Map<String, String> values, @PathVariable("token") String token){
        User cUser = tokenStore.getUser(token);
        if(cUser != null) {
            try {
                int teacherId = Integer.parseInt(values.get("teacherId"));
                int communicationRating = Integer.parseInt(values.get("communicationRating"));
                int teachingRating = Integer.parseInt(values.get("teachingRating"));
                int recommendRating = Integer.parseInt(values.get("recommendRating"));

                User teacher = userRepository.findById(teacherId);
                Rating rating = new Rating(cUser, teacher, communicationRating, teachingRating, recommendRating);
                ratingRepository.save(rating);
                return success;
            } catch (NumberFormatException e) {
                return "Invalid input format. Please ensure all ratings are numeric.";
            }
        }
        return failure;
    }

    @GetMapping("/getTeacherRatings/{teacherId}")
    public List<Rating> getTeacherRatings(@PathVariable("teacherId") int teacherId){
        return ratingRepository.findAllRatingByTeacherId(teacherId);
    }

    @GetMapping("/getAverageTeacher/{teacherId}")
    public ResponseEntity<?> getAverageTeacher(@PathVariable("teacherId") int teacherId){
        List<Rating> rates = ratingRepository.findAllRatingByTeacherId(teacherId);
        if (rates.isEmpty()) {
            // Return 404 Not Found if there are no ratings for the teacher
            return ResponseEntity.notFound().build();
        } else {
            int total = 0;
            for(Rating r : rates){
                total += r.getOverall_rating();
            }
            // Convert the average to an int, truncating the decimal part
            int average = total / rates.size();
            
            // Prepare the response in the desired format
            Map<String, Integer> response = new HashMap<>();
            response.put("rating", average);
            
            // Return 200 OK with the response map
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/deleteRating/{token}")
    public String deleteRating(@PathVariable("token") String token){
        User user = tokenStore.getUser(token);
        if(!user.getRole().equals(Roles.UserRoles.ADMIN)){
            return failure;
        }
        if(ratingRepository != null){
            List<Rating> ratings = ratingRepository.findAll();
            for(Rating r : ratings){
                ratingRepository.deleteById(r.getId());
            }
            return success;
        }
        return failure;
    }


    @PutMapping("/updateRating/{ratingId}")
    public String updateRating(@RequestBody Map<String, String> values, @PathVariable("ratingId") int ratingId) {
        Rating rating = ratingRepository.findById(ratingId);
        if (rating != null) {
            try {
                int communicationRating = Integer.parseInt(values.get("communicationRating"));
                int teachingRating = Integer.parseInt(values.get("teachingRating"));
                int recommendRating = Integer.parseInt(values.get("recommendRating"));

                rating.setCommunication_rating(communicationRating);
                rating.setTeaching_rating(teachingRating);
                rating.setRecommend_rating(recommendRating);
                rating.setOverall_rating((communicationRating + teachingRating + recommendRating) / 3);
                ratingRepository.save(rating);
                return success;
            } catch (NumberFormatException e) {
                return "Invalid input format. Please ensure all ratings are numeric.";
            }
        }
        return failure;
    }


    @GetMapping("/getAllRatings")
    public List<Rating> getAllRatings(){
        return ratingRepository.findAll();
    }

    @GetMapping("/getAllStudentRatings/{token}")
    public List<Rating> getAllStudentRatings(@PathVariable(name = "token") String token){
        User student = tokenStore.getUser(token);
        List<Rating> ratings = ratingRepository.findAllRatingByStudentId(student.getId());
        return ratings;
    }

    @DeleteMapping("/deleteAllRatings")
    public String deleteAllRatings(){
        if(!ratingRepository.findAll().isEmpty()){
            ratingRepository.deleteAll();
            return "success";
        }
        return "failure";
    }

}
