package code.Booking;

import code.Achievements.AchievementRepository;
import code.Subject.Subject;
import code.Subject.SubjectRepository;
import code.Users.TokenStore;
import code.Users.User;
import code.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class BookingController {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    AchievementRepository achievementRepository;
    
    @Autowired
    TokenStore tokenStore;


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @PostMapping("/createBooking/{token}")
    public String createBooking(@RequestBody BookingRequest bookingRequest) {
        try {
            Optional<User> teacher = Optional.ofNullable(userRepository.findById(bookingRequest.getTeacher().getId()));
            Optional<User> student = Optional.ofNullable(userRepository.findById(bookingRequest.getStudent().getId()));
            Optional<Subject> subject = subjectRepository.findById(bookingRequest.getSubject().getSubjectID());

            if (teacher.isEmpty() || student.isEmpty() || subject.isEmpty()) {
                return failure;
            }

            Booking newBooking = new Booking(subject.get(), teacher.get(), student.get(), bookingRequest.getDate(), bookingRequest.getStartTime(), bookingRequest.getEndTime());
            bookingRepository.save(newBooking);

            // Achievement
            User stdnt = userRepository.findById(bookingRequest.getStudent().getId());
            stdnt.setNumberOfBookings(stdnt.getNumberOfBookings() + 1);
            if(stdnt.getNumberOfBookings() == 1){
                stdnt.addAchievement(1);
            }
            else if(stdnt.getNumberOfBookings() == 5){
                stdnt.addAchievement(2);
            }
            else if (stdnt.getNumberOfBookings() == 10) {
                stdnt.addAchievement(3);
            }
            userRepository.save(stdnt);
            //

            return success;
        } catch (Exception e) {
            System.err.println("Error creating booking: " + e.getMessage());
            return failure;
        }
    }


    @GetMapping("/printBookingInfo")
    public List<Booking> printBookingInfo(){
        return bookingRepository.findAll();
    }


    @PutMapping("/updateBookingInfo")
    public ResponseEntity<String> updateBookingInfo(@RequestBody BookingRequest bookingRequest) {
        // Find the current booking by ID
        Optional<Booking> currentBookingOptional = bookingRepository.findBookingByID(bookingRequest.getID());

        // Check if the current booking is present
        if (currentBookingOptional.isEmpty()) {
            // Return failure response if not found
            return new ResponseEntity<>("Booking not found", HttpStatus.NOT_FOUND);
        }

        // Get the current booking from optional
        Booking currentBooking = currentBookingOptional.get();

        // Set the new properties on the current booking
        currentBooking.setSubject(bookingRequest.getSubject());
        currentBooking.setStartTime(bookingRequest.getStartTime());
        currentBooking.setEndTime(bookingRequest.getEndTime());
        currentBooking.setDate(bookingRequest.getDate());

        // Save the updated booking back to the repository
        bookingRepository.save(currentBooking);

        // Return a success response
        return new ResponseEntity<>(success, HttpStatus.OK);
    }


    @DeleteMapping("/deleteBooking/{id}")
    public String deleteBooking(@PathVariable int id) {
        Optional<Booking> currentBookingOptional = bookingRepository.findBookingByID(id);

        if (currentBookingOptional.isEmpty()) {
            // Return failure response if not found
            return failure;
        }

        // Get the booking from optional
        Booking bookingToDelete = currentBookingOptional.get();
        bookingRepository.delete(bookingToDelete);

        return success;
    }


    @DeleteMapping("/deleteAllBooking")
    public String deleteAllBooking(){
        if(!bookingRepository.findAll().isEmpty()){
            bookingRepository.deleteAll();
            return "success";
        }
        return "failure";
    }

    @GetMapping("/getBookingsByUser/{token}")
    public List<Booking> getBookingsByUser(@PathVariable("token") String token){
        User user = tokenStore.getUser(token);
        List<Booking> all = bookingRepository.findAll();
        List<Booking> ret = new ArrayList<Booking>();
        for (Booking b : all){
            if (b.getStudent().getId() == user.getId()){
                ret.add(b);
            }
        }
        return ret;
    }

    @GetMapping("/getBookingsByTeacher/{token}")
    public List<Booking> getBookingsByTeacher(@PathVariable("token") String token){
        User user = tokenStore.getUser(token);
        List<Booking> all = bookingRepository.findAll();
        List<Booking> ret = new ArrayList<Booking>();
        for (Booking b : all){
            if (b.getTeacher().getId() == user.getId()){
                ret.add(b);
            }
        }
        return ret;
    }



}
