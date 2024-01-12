package server.acode.domain.family.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import server.acode.domain.family.dto.request.FragranceFilterCond;
import server.acode.domain.family.dto.response.DisplayFragrance;
import server.acode.domain.family.dto.response.HomeFragrance;

import java.util.List;

@Repository
public interface FamilyRepositoryCustom {
    List<HomeFragrance> search(String familyCond);
    Page<DisplayFragrance> searchByFilter(FragranceFilterCond cond, String additionalFamily, Pageable pageable);
    Page<DisplayFragrance> searchByIngredient(String ingredientName, Pageable pageable);
}
