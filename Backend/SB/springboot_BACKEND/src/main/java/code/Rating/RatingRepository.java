package code.Rating;

import code.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface RatingRepository extends JpaRepository<Rating, Integer> {
    Rating findById(int id);

    @Transactional
    void deleteById(int id);

    List<Rating> findAllRatingByTeacherId(int id);

    List<Rating> findAllRatingByStudentId(int id);

}
