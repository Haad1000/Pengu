package code.Reports;

import code.Users.Roles;
import code.Users.TokenStore;
import code.Users.User;
import code.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ReportController {

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenStore tokenStore;


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @PostMapping("/createReport/{token}")
    public String createReport(@PathVariable String token, @RequestBody ReportRequest reportRequest) {
        User student = tokenStore.getUser(token);
        if(student == null) {
            return failure; // No user associated with the provided token
        }
        User teacher = userRepository.findById(reportRequest.getTeacherReportedID());
        if (teacher != null) {
            Report report = new Report(student, teacher, reportRequest.getTitle(), reportRequest.getDescription(), ReportStatus.ONGOING);
            reportRepository.save(report);
            return success;
        }
        return failure;
    }

    @PutMapping("/updateReport/{id}")
    public String updateReport(@PathVariable("id") int id) {
        Report currentReport = reportRepository.findReportById(id);
        if(currentReport != null) {
            currentReport.setStatus(ReportStatus.DONE);
            reportRepository.save(currentReport);
            return success;
        }
        return failure;
    }

    @DeleteMapping("/deleteAllReports/{token}")
    public String deleteAllReports(@PathVariable String token) {
        User user = tokenStore.getUser(token);
        if(user == null || !user.getRole().equals(Roles.UserRoles.ADMIN)) { // Assuming an ADMIN role check
            return failure; // Not authorized or no user associated with the provided token
        }
        List<Report> allReports = reportRepository.findAll();
        for (Report report : allReports) {
            reportRepository.deleteById(report.getId());
        }
        return success;
    }

    @GetMapping("/getReports/{studentId}")
    public List<Report> getReportsByStudent(@PathVariable("studentId") int studentId) {
        User student = userRepository.findById(studentId);
        if (student != null) {
            return reportRepository.findReportsByStudentReporting(student);
        }
        return new ArrayList<>(); // return an empty list if the student does not exist
    }

    @GetMapping("/getAllReports")
    public List<Report> getAllReports(){
        List<Report> allReports = reportRepository.findAll();
        return allReports;
    }

    @DeleteMapping("/deleteAllReports")
    public String deleteAllReports(){
        if(!reportRepository.findAll().isEmpty()){
            reportRepository.deleteAll();
            return "success";
        }
        return "failure";
    }


}
