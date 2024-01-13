package server.acode.domain.ingredient.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    private String backgroundImg; // 배경 url

    //나중에 줄글 길면 도메인 수정해야 할 수도 있음
    private String summary; // 향료 설명 줄글
    private String keyword; // 향료 키워드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_type_id")
    private IngredientType ingredientType;
}
