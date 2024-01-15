package server.acode.domain.fragrance.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;
import server.acode.domain.fragrance.entity.Fragrance;

import java.util.List;

@Getter
public class GetFragranceReview {
    private Long fragranceId;
    private int rateSum;
    private int reviewCnt;

    private int totalPages;
    private long totalElements;
    private List<ReviewInfo> reviewInfoList;

    public GetFragranceReview(Long fragranceId, int rateSum, int reviewCnt, int totalPages, long totalElements, List<ReviewInfo> reviewInfoList) {
        this.fragranceId = fragranceId;
        this.rateSum = rateSum;
        this.reviewCnt = reviewCnt;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.reviewInfoList = reviewInfoList;
    }

    public static GetFragranceReview from(Fragrance fragrance, Page<ReviewInfo> reviewInfoPage) {
        return new GetFragranceReview(
                fragrance.getId(), fragrance.getRateSum(), fragrance.getReviewCnt(),
                reviewInfoPage.getTotalPages(), reviewInfoPage.getTotalElements(), reviewInfoPage.getContent()
        );
    }

}
