package code.Reports;

import code.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author Vivek Bengre
 *
 */

public interface ReportRepository extends JpaRepository<Report, Long> {
    Report findReportById(int id);

    List<Report> findReportsByStudentReporting(User student);

    @Transactional
    void deleteById(int id);


}
