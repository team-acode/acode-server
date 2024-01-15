package server.acode.domain.review.repository;

import org.springframework.stereotype.Repository;
import server.acode.domain.fragrance.dto.response.ReviewPreview;
import server.acode.domain.fragrance.entity.Fragrance;

import java.util.List;

@Repository
public interface ReviewRepositoryCustom {
    List<ReviewPreview> getFragranceReviewPreview(Fragrance fragrance);
}
