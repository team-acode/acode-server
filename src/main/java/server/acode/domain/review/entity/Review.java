package server.acode.domain.review.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.review.entity.enums.Intensity;
import server.acode.domain.review.entity.enums.Longevity;
import server.acode.domain.review.entity.enums.SeasonEnum;
import server.acode.domain.review.entity.enums.StyleEnum;
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

    private String comment; // 한 줄 리뷰

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private SeasonEnum seasonEnum; // 계절감

    @Column(length = 25)
    @Enumerated(EnumType.STRING)
    private Longevity longevity; // 지속성

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private Intensity intensity; // 향의 세기

    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private StyleEnum style; // 스타일

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
}
