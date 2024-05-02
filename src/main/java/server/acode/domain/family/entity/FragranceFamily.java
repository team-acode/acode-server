package server.acode.domain.family.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.global.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {@Index(name = "idx_fragrancefamily_fragance_id", columnList = "fragrance_id"),
        @Index(name = "idx_fragrancefamily_family_id", columnList = "family_id")})
public class FragranceFamily extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fragrance_family_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fragrance_id")
    private Fragrance fragrance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;
}
