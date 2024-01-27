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
public class ReviewSeason extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_season_id")
    private Long id;

    @Column(columnDefinition = "integer default 0")
    private int spring;

    @Column(columnDefinition = "integer default 0")
    private int summer;

    @Column(columnDefinition = "integer default 0")
    private int autumn;

    @Column(columnDefinition = "integer default 0")
    private int winter;

    @Column(columnDefinition = "bigint default 0")
    @Version
    private long version;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fragrance_id")
    private Fragrance fragrance;
}
