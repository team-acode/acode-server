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
public class ReviewLongevity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_longevity_id")
    private Long id;

    @Column(columnDefinition = "integer default 0")
    private int onehour;

    @Column(columnDefinition = "integer default 0")
    private int fourhours;

    @Column(columnDefinition = "integer default 0")
    private int halfday;

    @Column(columnDefinition = "integer default 0")
    private int fullday;

    @Version
    private Long version;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fragrance_id")
    private Fragrance fragrance;

    public void updateVariable(String longevity, int value){
        switch (longevity){
            case "onehour":
                onehour += value;
                break;
            case "fourhours":
                fourhours += value;
                break;
            case "halfday":
                halfday += value;
                break;
            case "fullday":
                fullday += value;
                break;
        }
    }

}
