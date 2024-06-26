package code.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Vivek Bengre
 *
 */

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);

    @Transactional
    void deleteById(int id);

    User findByEmailId(String email);

}