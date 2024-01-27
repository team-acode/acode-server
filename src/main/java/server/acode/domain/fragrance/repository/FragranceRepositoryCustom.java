package server.acode.domain.fragrance.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FragranceRepositoryCustom {
    List<Long> extractByConcentration(String concentration1, String concentration2);

    List<Long> extractByConcentrationOr(String concentration1, String concentration2);

    List<Long> extractBySeason(String season1, String season2, List<Long> fragranceIdList);

    List<Long> extractBySeasonOr(String season1, String season2, List<Long> fragranceIdList);

    List<Long> extractByConcentrationAndSeason(String concentraionCond, String seasonCond);

    List<Long> extractByScent(String scent1, String scent2, List<Long> fragranceIdList);

    List<Long> extractByScentOr(String scent1, String scent2, List<Long> fragranceIdList);

//    List<Long> extractByScent(String scent, List<Long> fragranceIdList);

    List<Long> extractByStyle(String style1, String style2, List<Long> fragranceIdList);

    List<Long> extractByStyleOr(String style1, String style2, List<Long> fragranceIdList);

//    List<Long> extractByStyle(String style, List<Long> fragranceIdList);
}