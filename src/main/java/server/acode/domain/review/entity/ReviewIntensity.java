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
public class ReviewIntensity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_intensity_id")
    private Long id;

    @Column(columnDefinition = "integer default 0")
    private int weak;

    @Column(columnDefinition = "integer default 0")
    private int medium;

    @Column(columnDefinition = "integer default 0")
    private int strong;

    @Column(columnDefinition = "integer default 0")
    private int intense;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fragrance_id")
    private Fragrance fragrance;

    @Column(columnDefinition = "bigint default 0")
    @Version
    private long version;
}
