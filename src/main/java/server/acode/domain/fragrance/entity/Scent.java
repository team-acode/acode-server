package server.acode.domain.fragrance.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.global.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scent extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scent_id")
    private Long id;

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean smoky; //스모키한

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean zesty; //상큼한

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean sweet; //달콤한

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean fruity; //과일향 나는

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean fresh; // 상쾌한

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean crisp; // 청량한

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean moist; // 촉촉한

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean cool; // 시원한

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean aromatic; // 쌉싸름한

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean cozy; // 포근한

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean spicy; // 스파이시한

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean smooth; // 부드러운

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean floral; // 플로럴한

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean lively; // 싱그러운

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean fragrant; // 향긋한

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean rich; // 풍부한

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean nature; // 자연 그대로의

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fragrance_id")
    private Fragrance fragrance;
}
