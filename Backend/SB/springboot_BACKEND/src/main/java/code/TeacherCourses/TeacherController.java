package code.TeacherCourses;

import code.Subject.Subject;
import code.Subject.SubjectRepository;
import code.Users.User;
import code.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TeacherController {

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    UserRepository userRepository;


    @GetMapping("/subjects/{id}")
    public List<Subject> addSubjectToTeachings(@PathVariable int id) {
       Teacher t =  teacherRepository.getOne(id);
        return t.getTeacherCourses();
    }

    @PutMapping("/subjects/{teach}/{subId}")
    public ResponseEntity<String> updateTeacherSubjects(@PathVariable int teach, @PathVariable int subId) {
        Teacher t =  teacherRepository.getOne(teach);

            Subject s = subjectRepository.findBySubjectID(subId);

            t.getTeacherCourses().add(s);
            teacherRepository.save(t);

            s.setTeacher(t);
            subjectRepository.save(s);


            return ResponseEntity.ok("Teacher's Subjects were successfully updated");

    }



    @PostMapping("/addTeachings")
    @Transactional
    public ResponseEntity<String> addTeachingSubject(@RequestBody TeacherRequest teacherRequest) {
        // Retrieve the subject details from the request
        TeacherRequest.SubjectRequest subjectRequest = teacherRequest.getSubjectRequest();

        // Find the subject based on the ID provided in the request
        Optional<Subject> subjectOptional = subjectRepository.findById(subjectRequest.getSubjectID());
        if (subjectOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Subject does not exist.");
        }
        Subject subject = subjectOptional.get();

        // Find the teacher by ID
        Optional<Teacher> teacherOptional = teacherRepository.findById(teacherRequest.getId());
        if (teacherOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Teacher does not exist.");
        }
        Teacher teacher = teacherOptional.get();

        // Check if the subject is already in the teacher's list
        if (teacher.getTeacherCourses().contains(subject)) {
            return ResponseEntity.badRequest().body("Subject is already assigned to the teacher.");
        }

        // Add the subject to the teacher's list
        teacher.getTeacherCourses().add(subject);
        // Set the teacher to the subject
        subject.setTeacher(teacher);

        // Save the teacher entity to commit the transaction
        teacherRepository.save(teacher);
        // Save the subject entity with the new teacher assignment
        subjectRepository.save(subject);

        return ResponseEntity.ok("Subject added successfully to the teacher's courses.");
    }


    @DeleteMapping("/delete/{teach}/{subId}")
    public ResponseEntity<String> deletingTeacherCourses(@PathVariable int teach, @PathVariable int subId){
        Teacher t = teacherRepository.getOne(teach);
        Subject s = subjectRepository.findBySubjectID(subId);

        // Example condition that could be tested as false
        if (s.getTeacher() != null && s.getTeacher().getId() != teach) {
            // This block can be used to return an error if the subject's assigned teacher is not the one specified.
            return ResponseEntity.badRequest().body("Subject is not assigned to this teacher.");
        }

        t.getTeacherCourses().remove(s);
        teacherRepository.save(t);
        s.setTeacher(null);
        subjectRepository.save(s);

        return ResponseEntity.ok("Subject was successfully deleted from teacher's course list");
    }

    @DeleteMapping("/deleteAllTeachers")
    public String deleteAllTeachers(){
        if(!teacherRepository.findAll().isEmpty()){
            teacherRepository.deleteAll();
            return "success";
        }
        return "failure";
    }


}
