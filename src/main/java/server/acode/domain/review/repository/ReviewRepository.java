package server.acode.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.acode.domain.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    int countByUserId(Long userId);
}
