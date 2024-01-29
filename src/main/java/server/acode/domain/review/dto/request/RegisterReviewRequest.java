package server.acode.domain.review.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.review.entity.Review;
import server.acode.domain.review.entity.enums.Intensity;
import server.acode.domain.review.entity.enums.Longevity;
import server.acode.domain.review.entity.enums.Season;
import server.acode.domain.user.entity.User;

@Getter
@NoArgsConstructor
public class RegisterReviewRequest {
    @NotNull
    private int rate;
    @NotNull
    private String comment;

    @NotNull
    private Season season;
    @NotNull
    private Longevity longevity;
    @NotNull
    private Intensity intensity;
    @NotNull
    private String style;

    private String textReview;
    private String thumbnail;
    private String image1;
    private String image2;

    public RegisterReviewRequest(int rate, String comment, String season, String longevity, String intensity, String style){
        this.rate = rate;
        this.comment = comment;
        this.season = Season.valueOf(season);
        this.longevity = Longevity.valueOf(longevity);
        this.intensity = Intensity.valueOf(intensity);
        this.style = style;
    }

    public Review toEntity(User user, Fragrance fragrance) {
        return Review.builder()
                .rate(rate).comment(comment)
                .season(season).longevity(longevity).intensity(intensity).style(style)
                .textReview(textReview).thumbnail(thumbnail).image1(image1).image2(image2)
                .user(user).fragrance(fragrance)
                .build();
    }
}
