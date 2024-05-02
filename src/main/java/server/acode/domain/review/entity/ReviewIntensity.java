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
@Table(indexes = @Index(name = "idx_reviewintensity_fragance_id", columnList = "fragrance_id"))
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

    @Version
    private Long version;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fragrance_id")
    private Fragrance fragrance;

    public void updateVariable(String intensity, int value){
        switch (intensity){
            case "weak":
                weak += value;
                break;
            case "medium":
                medium += value;
                break;
            case "strong":
                strong += value;
                break;
            case "intense":
                intense += value;
                break;
        }
    }

}
