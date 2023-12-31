package server.acode.domain.fragrance.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.global.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(columnList = "korBrand"))
public class Fragrance extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fragrance_id")
    private Long id;

    @Column(length = 50)
    private String name; // 향수 이름 한글

    private String korBrand; // 브랜드 한글 이름
    private String engBrand; // 브랜드 영어 이름


    private int rateSum; // 별점 총점
    private int reviewCnt; // 리뷰 수

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private Concentration concentration; // EDP, EDT, EDC

    private int view; // 조회수

    private String poster; //포스터 이미지 url

    private String link1; // 구매 링크 url
    private String link2;
    private String link3;

    private String thumbnail;  // 대표 사진 url
    private String image1;
    private String image2;

    private String style;  // 스타일
    private String season;  // 계절감
    private String scent;  // 향
}
