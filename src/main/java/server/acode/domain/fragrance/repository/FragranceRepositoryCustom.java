package server.acode.domain.fragrance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import server.acode.domain.family.dto.response.FragranceCatalogDto;
import server.acode.domain.fragrance.dto.ReviewUpdateDto;

import java.util.List;

@Repository
public interface FragranceRepositoryCustom {

    void findWithPessimisticLockById(Long fragranceId);

    Page<FragranceCatalogDto> searchByIngredient(String ingredientName, Pageable pageable);

    List<Long> extractByConcentration(String concentration1, String concentration2);

    List<Long> extractByConcentrationOr(String concentration1, String concentration2);

    List<Long> extractBySeason(String season1, String season2, List<Long> fragranceIdList);

    List<Long> extractBySeasonOr(String season1, String season2, List<Long> fragranceIdList);


    List<Long> extractByConcentrationAndSeason(String concentraionCond, String seasonCond);

    List<Long> extractByOnlySeasonOr(String season1, String season2);

    List<Long> extractByScent(String scent1, String scent2, List<Long> fragranceIdList);

    List<Long> extractByOnlyScent(String scent1, String scent2);

    List<Long> extractByScentOr(String scent1, String scent2, List<Long> fragranceIdList);

//    List<Long> extractByScent(String scent, List<Long> fragranceIdList);

    List<Long> extractByOnlyScentOr(String scent1, String scent2);

    List<Long> extractByStyle(String style1, String style2, List<Long> fragranceIdList);

    List<Long> extractByOnlyStyle(String style1, String style2);

    List<Long> extractByStyleOr(String style1, String style2, List<Long> fragranceIdList);

    List<Long> extractByOnlyStyleOr(String style1, String style2);

//    List<Long> extractByStyle(String style, List<Long> fragranceIdList);
}