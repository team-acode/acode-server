package server.acode.domain.fragrance.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.domain.ingredient.entity.Ingredient;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoteInfo {
    private Long ingredientId;
    private String ingredientName;
    private String acode; // 어코드 설명

    public NoteInfo(Ingredient ingredient) {
        this.ingredientId = ingredient.getId();
        this.ingredientName = ingredient.getKorName();
        this.acode = ingredient.getAcode();
    }
}
