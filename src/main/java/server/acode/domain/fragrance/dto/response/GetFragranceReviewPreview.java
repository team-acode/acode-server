package server.acode.domain.fragrance.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.domain.fragrance.entity.Fragrance;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetFragranceReviewPreview {
    private Long fragranceId;
    private int reviewCnt;
    private int rateSum;
    private List<ReviewPreview> reviewPreviewList;

    public GetFragranceReviewPreview(Fragrance fragrance, List<ReviewPreview> reviewPreviewList) {
        this.fragranceId = fragrance.getId();
        this.reviewCnt = fragrance.getReviewCnt();
        this.rateSum = fragrance.getRateSum();
        this.reviewPreviewList = reviewPreviewList;
    }
}
