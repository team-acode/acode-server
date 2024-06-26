package server.acode.domain.family.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import server.acode.domain.family.dto.SimilarFragranceOrCond;
import server.acode.domain.family.dto.request.FragranceFilterCond;
import server.acode.domain.family.dto.response.FragranceCatalogDto;
import server.acode.domain.family.dto.response.HomeFragranceDto;
import server.acode.domain.fragrance.dto.request.SearchCond;
import server.acode.domain.fragrance.dto.response.ExtractFragrance;
import server.acode.domain.fragrance.dto.response.FamilyCountDto;
import server.acode.domain.fragrance.dto.response.FragranceInfo;

import java.util.List;

@Repository
public interface FragranceFamilyRepositoryCustom {
    List<HomeFragranceDto> search(String familyName);

    Page<FragranceCatalogDto> searchByBrandAndFamily(FragranceFilterCond cond, Pageable pageable);

    List<Long> searchFamilyByFragranceId(Long fragranceId);

    List<FragranceInfo> searchSimilarFragranceAnd(Long fragranceId, Long familyId1, Long familyId2);

    List<FragranceInfo> searchSimilarFragranceOr(SimilarFragranceOrCond cond);

    List<FragranceInfo> searchSimilarFragrance(Long fragranceId, Long familyId);

    List<Long> extractByMainFamily(String mainFamily1, String mainFamily2, List<Long> fragranceIdList);

    List<Long> extractByMainFamilyOr(String mainFamily1, String mainFamily2, List<Long> fragranceIdList);

    List<FamilyCountDto> countFamily(List<Long> fragranceIdList);

    List<ExtractFragrance> extractFragrance(List<Long> familyIdList);

    Page<FragranceInfo> searchFragrance(SearchCond cond, String additionalFamily, Pageable pageable);
    List<Long> extractByOnlyMainFamilyOr(String mainFamily1, String mainFamily2);
}
