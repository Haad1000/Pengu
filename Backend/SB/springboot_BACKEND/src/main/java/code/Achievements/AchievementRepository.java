package code.Achievements;

import code.Biography.Biography;
import code.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Achievement findById(int id);

    @Transactional
    void deleteById(int id);
}
