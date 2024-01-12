package server.acode.domain.ingredient.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import server.acode.domain.ingredient.dto.response.IngredientOfTheDay;
import server.acode.domain.ingredient.dto.response.QIngredientOfTheDay;
import server.acode.domain.ingredient.entity.QIngredient;

import java.util.List;

import static server.acode.domain.ingredient.entity.QIngredient.*;


@Repository
public class IngredientRepositoryImpl implements IngredientRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public IngredientRepositoryImpl(EntityManager em) { this.queryFactory = new JPAQueryFactory(em); }
    @Override
    public List<IngredientOfTheDay> getTodayIngreient() {
        return queryFactory
                .select(new QIngredientOfTheDay(
                        ingredient.korName.as("ingredientName"),
                        ingredient.acode
                ))
                .from(ingredient)
                .where(ingredient.id.in(5, 100, 80, 1, 102))
                .fetch();
    }
}
