package server.acode.domain.ingredient.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class IngredientOfTheDay {
    private String ingredientName;
    private String acode;

    @QueryProjection
    public IngredientOfTheDay(String ingredientName, String acode){
        this.ingredientName = ingredientName;
        this.acode = acode;
    }
}
