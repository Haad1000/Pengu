package code.Subject;

import code.Review_FINAL.ReviewRepository;
import code.TeacherCourses.Teacher;
import code.Users.TokenStore;
import code.Users.User;
import code.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import code.Users.Roles;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@RestController
public class SubjectController {
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    TokenStore tokenStore;


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @PostMapping("/createSubject/{token}")
    public String createSubject(@PathVariable String token, @RequestBody SubjectRequest subjectRequest) {
        User user = tokenStore.getUser(token);
        if (user != null && user.getRole() == Roles.UserRoles.ADMIN) {
            Subject newSubject = new Subject(subjectRequest.getSubjectName());
            subjectRepository.save(newSubject);
            return success;
        } else {
            return failure;
        }
    }

    @DeleteMapping("/deleteSubject/{subjectId}/{token}")
    public String deleteSubject(@PathVariable int subjectId, @PathVariable String token) {
        User user = tokenStore.getUser(token);
        if (user != null && user.getRole() == Roles.UserRoles.ADMIN) {
            subjectRepository.deleteSubjectBySubjectID(subjectId);
            return success;
        } else {
            return failure;
        }
    }

    @PutMapping("/updateSubject/{token}")
    public String updateSubject(@PathVariable String token, @RequestBody SubjectRequest subjectRequest) {
        User user = tokenStore.getUser(token);
        if (user != null && user.getRole() == Roles.UserRoles.ADMIN) {
            Subject currentSubject = subjectRepository.findBySubjectID(subjectRequest.getSubjectID());
            if (currentSubject != null) {
                currentSubject.setSubjectName(subjectRequest.getSubjectName());
                subjectRepository.save(currentSubject);
                return success;
            }
        }
        return failure;
    }

    @GetMapping("/printSubject")
    public ArrayList<Subject> printSubject(){
        ArrayList<Subject> subjectList = new ArrayList<Subject>();
        subjectList.addAll(subjectRepository.findAll());
        return subjectList;
    }

    @DeleteMapping("/deleteAllSubjects")
    public String deleteAllSubjects(){
        if(!subjectRepository.findAll().isEmpty()){
            subjectRepository.deleteAll();
            return "success";
        }
        return "failure";
    }

    @GetMapping("/getTeachersBySubject/{subjectId}")
    public List<User> getTeachersBySubject(@PathVariable("subjectId") int subjectId){
        Subject subject = subjectRepository.findBySubjectID(subjectId);
        List<User> list = userRepository.findAll();
        List<User> ret = new ArrayList<User>();
        for(User t : list){
            if(t.getId() == subject.getTeacher().getId()){
                ret.add(t);
            }
        }
        return ret;
    }

}

