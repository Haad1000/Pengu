package code.TeacherSlots;

import code.TeacherCourses.Teacher;
import code.Users.TokenStore;
import code.Users.User;
import code.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class SlotController {

    @Autowired
    TokenStore tokenStore;

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TimeSlotRangeRepository timeSlotRangeRepository;

    private List<TimeSlotRange> availableS = new ArrayList<>();

    @PostMapping("/slots/open/{token}/{date}")
    public ResponseEntity<String> openSlots(
            @RequestBody List<TimeSlotRange> slotRanges,
            @PathVariable("token") String token,
            @PathVariable("date") String dateString) {

        // Parse the date string to a LocalDate object
        LocalDate date;
        try {
            date = LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Please use YYYY-MM-DD.");
        }

        // Authenticate the teacher using the token
        User user = tokenStore.getUser(token);
        if (!(user instanceof Teacher)) {
            return ResponseEntity.badRequest().body("Invalid token or user not authorized.");
        }
        Teacher teacher = (Teacher) user;

        // Create a new Slot instance and set the date
        Slot slot = new Slot();
        slot.setDate(date);

        // Adjust each TimeSlotRange, setting the teacher and associating it with the Slot
        for (TimeSlotRange timeSlotRange : slotRanges) {
            timeSlotRange.setTeacher(teacher); // Set the teacher in the TimeSlotRange
            timeSlotRange.setSlot(slot);       // Associate each TimeSlotRange with the Slot
            slot.getAvailableSlots().add(timeSlotRange); // Add each TimeSlotRange to the Slot
        }

        // Save the slot instance with associated time slot ranges
        slotRepository.save(slot);

        return ResponseEntity.ok("Time slots opened successfully.");
    }

    @PutMapping("/slots/update/{slotId}/{timeSlotRangeId}/{token}")
    public ResponseEntity<String> updateSlotAvailability(
            @PathVariable Integer slotId,
            @PathVariable Long timeSlotRangeId,
            @PathVariable String token) {

        // ... authentication logic ...

        // Find the slot with the provided ID
        Optional<Slot> slotOptional = slotRepository.findById(slotId);
        if (slotOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Slot not found.");
        }

        Slot slot = slotOptional.get();
        boolean updated = false;

        // Iterate through the time slot ranges within the slot
        for (TimeSlotRange range : slot.getAvailableSlots()) {
            // Check if the range's ID matches the path variable 'timeSlotRangeId'
            if (range.getId().equals(timeSlotRangeId)) {
                // Check current availability and update if needed
                if (!range.isAvailable()) {
                    range.setAvailable(true); // Make the specific time slot available
                    updated = true;
                }
                // Break after updating the found range
                break;
            }
        }

        // Save the entire slot along with its updated time slot range
        if (updated) {
            slotRepository.save(slot); // This will persist changes to the database
            return ResponseEntity.ok("Time slot availability updated and added to the available slots successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Time slot range not found within the specified slot.");
        }
    }


    @GetMapping("/slots/available/{teacherId}")
    public ResponseEntity<List<Map<String, Object>>> getAvailableSlots(@PathVariable("teacherId") int teacherId) {
        User teacher = userRepository.findById(teacherId);
        if (teacher == null) {
            return ResponseEntity.notFound().build();  // Return not found if the teacher doesn't exist
        }

        List<TimeSlotRange> timeSlotRanges = timeSlotRangeRepository.findAll();
        List<Map<String, Object>> slotDetails = new ArrayList<>();

        for (TimeSlotRange timeSlot : timeSlotRanges) {
            if (timeSlot.getTeacher().equals(teacher) && timeSlot.isAvailable()) {
                Map<String, Object> details = new HashMap<>();
                details.put("slotId", timeSlot.getSlot().getId());
                details.put("date", timeSlot.getSlot().getDate());
                details.put("startTime", timeSlot.getStartTime());
                details.put("endTime", timeSlot.getEndTime());
                details.put("timeSlotId", timeSlot.getId());
                slotDetails.add(details);
            }
        }

        if (slotDetails.isEmpty()) {
            return ResponseEntity.noContent().build();  // Return no content if there are no available slots
        }

        return ResponseEntity.ok(slotDetails);  // Return the list of slot details
    }

    @GetMapping("/slots/allByDate/{teacherId}/{dayId}")
    public ResponseEntity<List<Map<String, Object>>> getAllSlotsByDate(@PathVariable("teacherId") int teacherId, @PathVariable("dayId") int dayId) {
        User teacher = userRepository.findById(teacherId);
        if (teacher == null) {
            return ResponseEntity.notFound().build();  // Return not found if the teacher doesn't exist
        }

        List<TimeSlotRange> timeSlotRanges = timeSlotRangeRepository.findAll();
        List<Map<String, Object>> slotDetails = new ArrayList<>();

        for (TimeSlotRange timeSlot : timeSlotRanges) {
            if (timeSlot.getTeacher().equals(teacher) && timeSlot.getSlot().getId() == dayId) {
                Map<String, Object> details = new HashMap<>();
                details.put("slotId", timeSlot.getSlot().getId());
                details.put("date", timeSlot.getSlot().getDate());
                details.put("startTime", timeSlot.getStartTime());
                details.put("endTime", timeSlot.getEndTime());
                details.put("timeSlotId", timeSlot.getId());
                slotDetails.add(details);
            }
        }

        if (slotDetails.isEmpty()) {
            return ResponseEntity.noContent().build();  // Return no content if there are no slots for the teacher
        }

        return ResponseEntity.ok(slotDetails);  // Return the list of slot details
    }


    @GetMapping("/slots/all/{teacherId}")
    public ResponseEntity<List<Map<String, Object>>> getAllSlots(@PathVariable("teacherId") int teacherId) {
        User teacher = userRepository.findById(teacherId);
        if (teacher == null) {
            return ResponseEntity.notFound().build();  // Return not found if the teacher doesn't exist
        }

        List<TimeSlotRange> timeSlotRanges = timeSlotRangeRepository.findAll();
        List<Map<String, Object>> slotDetails = new ArrayList<>();

        for (TimeSlotRange timeSlot : timeSlotRanges) {
            if (timeSlot.getTeacher().equals(teacher)) {
                Map<String, Object> details = new HashMap<>();
                details.put("slotId", timeSlot.getSlot().getId());
                details.put("date", timeSlot.getSlot().getDate());
                details.put("startTime", timeSlot.getStartTime());
                details.put("endTime", timeSlot.getEndTime());
                details.put("timeSlotId", timeSlot.getId());
                slotDetails.add(details);
            }
        }

        if (slotDetails.isEmpty()) {
            return ResponseEntity.noContent().build();  // Return no content if there are no slots for the teacher
        }

        return ResponseEntity.ok(slotDetails);  // Return the list of slot details
    }





}

