package server.acode.domain.ingredient.repository;

import org.springframework.stereotype.Repository;
import server.acode.domain.ingredient.dto.response.IngredientOfTheDay;

import java.util.List;

@Repository
public interface IngredientRepositoryCustom {
    List<IngredientOfTheDay> getIngredientsOfTheDay();
}
