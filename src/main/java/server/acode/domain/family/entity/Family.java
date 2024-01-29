package server.acode.domain.family.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.global.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx_family_korname", columnList = "kor_name"))
public class Family extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_id")
    private Long id;

    @Column(length = 50)
    private String korName; // 향 계열 한글이름

    @Column(length = 50)
    private String engName; // 향 계열 영어이름

    private String summary; // 계열 설명 줄글

    private String icon; // 아이콘 이미지 url

    private String backgroundImg; // 배경 이미지 url

    private String keyword; // 키워드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_family_id")
    private MainFamily mainFamily;
}
