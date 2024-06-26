package code.Subject;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;


public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    Subject findBySubjectID(int ID);

    @Transactional
    void deleteSubjectBySubjectID(int ID);

}


