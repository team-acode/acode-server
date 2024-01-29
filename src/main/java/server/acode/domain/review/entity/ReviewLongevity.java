package server.acode.domain.review.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.global.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewLongevity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_longevity_id")
    private Long id;

    @Column(columnDefinition = "integer default 0")
    private int onehour;

    @Column(columnDefinition = "integer default 0")
    private int fourhours;

    @Column(columnDefinition = "integer default 0")
    private int halfday;

    @Column(columnDefinition = "integer default 0")
    private int fullday;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fragrance_id")
    private Fragrance fragrance;

}
