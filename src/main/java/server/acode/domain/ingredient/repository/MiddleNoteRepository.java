package server.acode.domain.ingredient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.ingredient.entity.Ingredient;
import server.acode.domain.ingredient.entity.MiddleNote;

import java.util.List;

@Repository
public interface MiddleNoteRepository extends JpaRepository<MiddleNote, Long> {
    @Query("SELECT mn.ingredient FROM MiddleNote mn WHERE mn.fragrance = :fragrance")
    List<Ingredient> findByFragrance(@Param("fragrance") Fragrance fragrance);
}
