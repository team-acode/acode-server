package server.acode.domain.fragrance.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.global.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Season extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "season_id")
    public Long id;

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean spring;

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean summer;

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean autumn;

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean winter;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fragrance_id")
    private Fragrance fragrance;
}
