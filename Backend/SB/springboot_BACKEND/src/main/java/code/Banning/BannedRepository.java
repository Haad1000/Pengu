package code.Banning;

import code.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface BannedRepository extends JpaRepository<Banned, Long> {
    Banned findById(int id);

    @Transactional
    void deleteById(int id);

    Banned findByEmailId(String email);

}
