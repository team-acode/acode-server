package server.acode.domain.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DisplayReview {
    private Long reviewId;
    private Long fragranceId;
    private String fragranceName;
    private String brandName;
    private String comment;
    private int rate;
    private String thumbnail;

    @QueryProjection
    public DisplayReview(Long reviewId, Long fragranceId, String fragranceName, String brandName, String comment, int rate, String thumbnail){
        this.reviewId = reviewId;
        this.fragranceId = fragranceId;
        this.fragranceName = fragranceName;
        this.brandName = brandName;
        this.comment = comment;
        this.rate = rate;
        this.thumbnail = thumbnail;
    }
}
