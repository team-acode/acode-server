package server.acode.domain.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.review.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    List<Review> findByFragrance(Fragrance fragrance);

    int countByUserId(Long userId);

}
