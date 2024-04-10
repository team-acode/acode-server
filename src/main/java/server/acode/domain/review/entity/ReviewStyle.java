package server.acode.domain.review.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.global.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewStyle extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_style_id")
    private Long id;

    @Column(columnDefinition = "integer default 0")
    private int chic; //시크한

    @Column(columnDefinition = "integer default 0")
    private int mature; //성숙한

    @Column(columnDefinition = "integer default 0")
    private int luxurious; //고급스러운

    @Column(columnDefinition = "integer default 0")
    private int elegant; //우아한

    @Column(columnDefinition = "integer default 0")
    private int masculine; //남성적인

    @Column(columnDefinition = "integer default 0")
    private int comfortable; //편안한

    @Column(columnDefinition = "integer default 0")
    private int serene; //차분한

    @Column(columnDefinition = "integer default 0")
    private int light; //가벼운

    @Column(columnDefinition = "integer default 0")
    private int neutral; //중성적인

    @Column(columnDefinition = "integer default 0")
    private int friendly; //친근한

    @Column(columnDefinition = "integer default 0")
    private int clean; //깨끗한

    @Column(columnDefinition = "integer default 0")
    private int sensual; //관능적인

    @Column(columnDefinition = "integer default 0")
    private int delicate; //은은한

    @Column(columnDefinition = "integer default 0")
    private int lively; //활기찬

    @Column(columnDefinition = "integer default 0")
    private int lovely; //사랑스러운

    @Column(columnDefinition = "integer default 0")
    private int bright; //밝은

    @Column(columnDefinition = "integer default 0")
    private int radiant; //화사한

    @Column(columnDefinition = "integer default 0")
    private int feminine; //여성스러운

    @Column(columnDefinition = "integer default 0")
    private int innocent; //청순한

    @Column(columnDefinition = "integer default 0")
    private int weighty; //무게감 있는

    @Column(columnDefinition = "integer default 0")
    private int soft; //부드러운

    @Column(columnDefinition = "integer default 0")
    private int cozy; //포근한

    @Version
    private Long version;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fragrance_id")
    private Fragrance fragrance;

    public void updateVariable(String[] styles, int value){
        // TODO 확장성 높이기
        for (int i = 0; i < styles.length; i++) {
            switch (styles[i]){
                case "chic":
                    chic += value;
                    break;
                case "mature":
                    mature += value;
                    break;
                case "luxurious":
                    luxurious += value;
                    break;
                case "elegant":
                    elegant += value;
                    break;
                case "masculine":
                    masculine += value;
                    break;
                case "comfortable":
                    comfortable += value;
                    break;
                case "serene":
                    serene += value;
                    break;
                case "light":
                    light += value;
                    break;
                case "neutral":
                    neutral += value;
                    break;
                case "friendly":
                    friendly += value;
                    break;
                case "clean":
                    clean += value;
                    break;
                case "sensual":
                    sensual += value;
                    break;
                case "delicate":
                    delicate += value;
                    break;
                case "lively":
                    lively += value;
                    break;
                case "lovely":
                    lovely += value;
                    break;
                case "bright":
                    bright += value;
                    break;
                case "radiant":
                    radiant += value;
                    break;
                case "feminine":
                    feminine += value;
                    break;
                case "innocent":
                    innocent += value;
                    break;
                case "weighty":
                    weighty += value;
                    break;
                case "soft":
                    soft += value;
                    break;
                case "cozy":
                    cozy += value;
                    break;
            }

        }
    }
}
