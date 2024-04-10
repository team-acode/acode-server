package server.acode.domain.ingredient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.acode.domain.ingredient.entity.Ingredient;

import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long>, IngredientRepositoryCustom {

    Optional<Ingredient> findByKorName(String korName);

    boolean existsByKorName(String ingredient);
}
