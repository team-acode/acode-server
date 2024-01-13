package server.acode.domain.fragrance.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.global.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long id;

    @Column(length = 50)
    private String korName; // 브랜드 한글 이름

    @Column(length = 50)
    private String engName; // 브랜드 영어 이름

    @Column(length = 50)
    private String summary; // 브랜드 설명
    private String keyword; // 브랜드 키워드
    private String backgroundImg; // 브랜드 백그라운드 이미지
    private String roundImg; // 브랜드 원형 이미지
}
