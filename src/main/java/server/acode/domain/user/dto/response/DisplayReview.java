package server.acode.domain.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DisplayReview {
    private Long reviewId;
    private String fragranceName;
    private String brandName;
    private String comment;
    private int rate;
    private String thumbnail;

    @QueryProjection
    public DisplayReview(Long reviewId, String fragranceName, String brandName, String comment, int rate, String thumbnail){
        this.reviewId = reviewId;
        this.fragranceName = fragranceName;
        this.brandName = brandName;
        this.comment = comment;
        this.rate = rate;
        this.thumbnail = thumbnail;
    }
}
