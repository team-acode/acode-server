package server.acode.domain.ingredient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.acode.domain.ingredient.entity.IngredientType;

@Repository
public interface IngredientTypeRepository extends JpaRepository<IngredientType, Long> {
}
