package server.acode.domain.family.dto.response;

import lombok.Getter;
import server.acode.domain.family.entity.Family;
import server.acode.domain.ingredient.entity.Ingredient;
import server.acode.domain.ingredient.entity.IngredientType;

import java.util.Arrays;
import java.util.List;

@Getter
public class DisplayIngredient {
    private String korName;
    private String engName;
    private String acode;
    private List<String> keyword;
    private String summary;
    private String background;
    private String ingredientType;
    private String icon;

    private DisplayIngredient(String korName, String engName, String acode, String keyword,
                              String summary, String background, String ingredientType,String icon){
        this.korName = korName;
        this.engName = engName;
        this.acode = acode;
        this.keyword = Arrays.asList(keyword.split(", "));
        this.summary = summary;
        this.background = background;
        this.ingredientType = ingredientType;
        this.icon = icon;
    }

    public static DisplayIngredient from(Ingredient ingredient, IngredientType type){
        return new DisplayIngredient(ingredient.getKorName(),
                ingredient.getEngName(),
                ingredient.getAcode(),
                ingredient.getKeyword(),
                ingredient.getSummary(),
                ingredient.getBackgroundImg(),
                type.getKorName(),
                type.getIcon());
    }
}
