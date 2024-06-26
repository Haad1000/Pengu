package code.Review_FINAL;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface ReviewRepository extends JpaRepository<Review, Long> {
     Review findReviewByID(int ID);
     @Transactional
     Review deleteReviewByID(int ID);

     Review findReviewByWrittenById(int ID);

     Review deleteReviewByWrittenById(int ID);


}
