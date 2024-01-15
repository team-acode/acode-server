package server.acode.domain.fragrance.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import server.acode.domain.review.entity.Review;
import server.acode.domain.review.entity.enums.Intensity;
import server.acode.domain.review.entity.enums.Longevity;
import server.acode.domain.review.entity.enums.Season;

@Data
public class ReviewInfo {
    private Long reviewId;

    private String thumbnail;
    private String image1;
    private String image2;

    private int rate;
    private String nickname;
    private String comment;
    private String textReview;

    private String season;
    private String longevity;
    private String intensity;
    private String Style;

    @QueryProjection
    public ReviewInfo(Long reviewId, String thumbnail, String image1, String image2,
                      int rate, String nickname, String comment, String textReview,
                      Season season, Longevity longevity, Intensity intensity, String style) {
        this.reviewId = reviewId;
        this.thumbnail = thumbnail;
        this.image1 = image1;
        this.image2 = image2;
        this.rate = rate;
        this.nickname = nickname;
        this.comment = comment;
        this.textReview = textReview;
        this.season = season.name();
        this.longevity = longevity.name();
        this.intensity = intensity.name();
        Style = style;
    }

    public static ReviewInfo from(Review review) {
        return new ReviewInfo(
                review.getId(), review.getThumbnail(), review.getImage1(), review.getImage2(),
                review.getRate(), review.getUser().getNickname(), review.getComment(), review.getTextReview(),
                review.getSeason(), review.getLongevity(), review.getIntensity(), review.getStyle()
        );
    }
}
