package server.acode.domain.ingredient.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.global.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IngredientType extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_type_id")
    private Long id;

    @Column(length = 50)
    private String korName; // 향료타입 한글이름
    @Column(length = 50)
    private String engName; // 향료타입 영어이름

    private String icon; // 아이콘 이미지 url
}
