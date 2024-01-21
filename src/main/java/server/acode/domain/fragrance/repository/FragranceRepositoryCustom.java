package server.acode.domain.fragrance.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FragranceRepositoryCustom {
    List<Long> extractByConcentrationAndSeason(String concentraionCond, String seasonCond);

    List<Long> extractByScent(String scent1, String scent2, List<Long> fragranceIdList);

    List<Long> extractByScentOr(String scent1, String scent2, List<Long> fragranceIdList);

//    List<Long> extractByScent(String scent, List<Long> fragranceIdList);

    List<Long> extractByStyle(String style1, String style2, List<Long> fragranceIdList);

    List<Long> extractByStyleOr(String style1, String style2, List<Long> fragranceIdList);

//    List<Long> extractByStyle(String style, List<Long> fragranceIdList);
}