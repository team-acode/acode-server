package server.acode.domain.ingredient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.ingredient.entity.Ingredient;
import server.acode.domain.ingredient.entity.TopNote;

import java.util.List;

@Repository
public interface TopNoteRepository extends JpaRepository<TopNote, Long> {
    @Query("SELECT tn.ingredient FROM TopNote tn WHERE tn.fragrance = :fragrance")
    List<Ingredient> findByFragrance(@Param("fragrance") Fragrance fragrance);
}
