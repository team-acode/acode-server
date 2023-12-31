package server.acode.domain.ingredient.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.domain.family.entity.Family;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.global.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ingredient extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Long id;

    @Column(length = 50)
    private String korName; // 향료 한글이름

    @Column(length = 50)
    private String engName; // 향료 영어이름

    private String acode; // 어코드 설명

    private String background; // 배경 url

    private String summary; // 향료 설명 줄글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_type_id")
    private IngredientType ingredientType;

}
