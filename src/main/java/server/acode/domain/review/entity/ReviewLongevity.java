package server.acode.domain.review.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.global.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewLongevity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_longevity_id")
    private Long id;

    @Column(columnDefinition = "integer default 0")
    private int oneHour;

    @Column(columnDefinition = "integer default 0")
    private int fourHours;

    @Column(columnDefinition = "integer default 0")
    private int halfDay;

    @Column(columnDefinition = "integer default 0")
    private int fullDay;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fragrance_id")
    private Fragrance fragrance;
}
