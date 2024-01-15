package server.acode.domain.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import server.acode.domain.fragrance.dto.response.ReviewInfo;
import server.acode.domain.fragrance.dto.response.ReviewPreview;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.user.dto.response.DisplayReview;

import java.util.List;

@Repository
public interface ReviewRepositoryCustom {
    List<ReviewPreview> getFragranceReviewPreview(Fragrance fragrance);

    Page<ReviewInfo> getReviewInfoPage(Long fragranceId, Pageable pageable);

    Page<DisplayReview> getDisplayReview(Long userId, Pageable pageable);
}
