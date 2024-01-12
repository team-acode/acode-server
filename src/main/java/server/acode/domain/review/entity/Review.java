package server.acode.domain.review.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.review.entity.enums.Intensity;
import server.acode.domain.review.entity.enums.Longevity;
import server.acode.domain.review.entity.enums.Season;
import server.acode.domain.user.entity.User;
import server.acode.global.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false)
    private int rate; // 별점

    @Column(nullable = false, length = 100)
    private String comment; // 한 줄 리뷰

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private Season season; // 계절감

    @Column(length = 25)
    @Enumerated(EnumType.STRING)
    private Longevity longevity; // 지속성

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private Intensity intensity; // 향의 세기

    @Column(length = 100)
    private String style; // 스타일

    @Column(length = 4000)
    private String textReview; // 텍스트 리뷰

    private String thumbnail; // 대표사진
    private String image1;
    private String image2;

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean isDel; // 삭제 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fragrance_id")
    private Fragrance fragrance;

    @Builder
    public Review(int rate, String comment,
                  String season, String longevity, String intensity, String style,
                  String textReview, String thumbnail, String image1, String image2,
                  User user, Fragrance fragrance) {
        this.rate = rate;
        this.comment = comment;

        this.season = Season.valueOf(season);
        this.longevity = Longevity.valueOf(longevity);
        this.intensity = Intensity.valueOf(intensity);
        this.style = style;

        this.textReview = textReview;
        this.thumbnail = thumbnail;
        this.image1 = image1;
        this.image2 = image2;

        this.user = user;
        this.fragrance = fragrance;
    }
}
