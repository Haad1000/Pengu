package code.Review_FINAL;

import code.Users.TokenStore;
import code.Users.User;
import code.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class Reviewcontroller {
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenStore tokenStore; // Assuming you have this service


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @PostMapping("/createReview/{token}")
    public String createReview(@PathVariable String token, @RequestBody ReviewRequest reviewRequest){
        User cUser = tokenStore.getUser(token);

        if (cUser == null) {
            return failure; // User not found or invalid token
        }

        User teacher1 = userRepository.findById(reviewRequest.getWrittenFor().getId());
        if (teacher1 == null) {
            return failure; // Teacher not found
        }

        Review newReview = new Review(cUser, teacher1, reviewRequest.getActualReview());
        reviewRepository.save(newReview);
        return success;
    }
    private Review updatedReview;

    @PutMapping("/updateReview/{token}")
    public String updateReview(@PathVariable String token, @RequestBody ReviewRequest reviewRequest){
        User cUser = tokenStore.getUser(token);

        if (cUser == null) {
            return failure; // User not found or invalid token
        }

        Review updatedReview = reviewRepository.findReviewByWrittenById(cUser.getId());
        if (updatedReview == null) {
            return failure; // Review not found
        }

        updatedReview.setActualReview(reviewRequest.getActualReview());
        reviewRepository.save(updatedReview);
        return success;
    }
    private Review retVal;
    @GetMapping("/printReview/{token}")
    public ResponseEntity<?> printReview(@PathVariable String token){
        User cUser = tokenStore.getUser(token);

        if (cUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(failure);
        }

        Review retVal = reviewRepository.findReviewByID(cUser.getId());
        return ResponseEntity.ok(retVal != null ? retVal : failure);
    }
    private Review currentReview;
    private Review deletedReview;
    @DeleteMapping("/deleteReview/{token}")
    public String deleteReview(@PathVariable String token) {
        User cUser = tokenStore.getUser(token);

        if (cUser == null) {
            return failure; // User not found or invalid token
        }

        Review reviewToDelete = reviewRepository.findReviewByWrittenById(cUser.getId());
        if (reviewToDelete == null) {
            return failure; // Review not found
        }

        reviewRepository.delete(reviewToDelete);
        return success;
    }

    @DeleteMapping("/deleteAllReviews")
    public String deleteAllReviews(){
        if(!reviewRepository.findAll().isEmpty()){
            reviewRepository.deleteAll();
            return "success";
        }
        return "failure";
    }

}
