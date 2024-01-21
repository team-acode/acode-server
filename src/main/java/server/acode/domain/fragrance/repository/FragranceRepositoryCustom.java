package server.acode.domain.fragrance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import server.acode.domain.family.dto.response.DisplayFragrance;

import java.util.List;

@Repository
public interface FragranceRepositoryCustom {
    Page<DisplayFragrance> searchByIngredient(String ingredientName, Pageable pageable);

    List<Long> extractByConcentrationAndSeason(String concentraionCond, String seasonCond);

    List<Long> extractByScent(String scent1, String scent2, List<Long> fragranceIdList);

    List<Long> extractByScentOr(String scent1, String scent2, List<Long> fragranceIdList);

//    List<Long> extractByScent(String scent, List<Long> fragranceIdList);

    List<Long> extractByStyle(String style1, String style2, List<Long> fragranceIdList);

    List<Long> extractByStyleOr(String style1, String style2, List<Long> fragranceIdList);

//    List<Long> extractByStyle(String style, List<Long> fragranceIdList);
}