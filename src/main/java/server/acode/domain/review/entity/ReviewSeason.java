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

    @Column(columnDefinition = "integer default 0")
    private int allSeasons;

    @Version
    private Long version;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fragrance_id")
    private Fragrance fragrance;

    public void updateVariable(String season, int value){
        switch (season){
            case "spring":
                spring += value;
                break;
            case "summer":
                summer += value;
                break;
            case "autumn":
                autumn += value;
                break;
            case "winter":
                winter += value;
                break;
            case "allseasons":
                allSeasons += value;
                break;
        }
    }
}
