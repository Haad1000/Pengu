package code.Biography;

import code.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Vivek Bengre
 *
 */

public interface BiographyRepository extends JpaRepository<Biography, Long> {
    Biography findById(int id);

    Biography findByUser(User user);

    Biography findByUserId(int id);

    @Transactional
    void deleteById(int id);
}