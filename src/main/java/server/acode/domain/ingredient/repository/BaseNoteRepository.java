package server.acode.domain.ingredient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.ingredient.entity.BaseNote;
import server.acode.domain.ingredient.entity.Ingredient;

import java.util.List;

@Repository
public interface BaseNoteRepository extends JpaRepository<BaseNote, Long> {
    @Query("SELECT bn.ingredient FROM BaseNote bn WHERE bn.fragrance = :fragrance")
    List<Ingredient> findByFragrance(@Param("fragrance") Fragrance fragrance);
}
