package server.acode.domain.family.repository;

import org.springframework.stereotype.Repository;
import server.acode.domain.family.dto.SimilarFragranceOrCond;
import server.acode.domain.fragrance.dto.response.ExtractFragrance;
import server.acode.domain.fragrance.dto.response.FamilyCountDto;
import server.acode.domain.fragrance.dto.response.FragranceInfo;

import java.util.List;

@Repository
public interface FragranceFamilyRepositoryCustom {
    List<Long> searchFamilyByFragranceId(Long fragranceId);

    List<FragranceInfo> searchSimilarFragranceAnd(Long fragranceId, Long familyId1, Long familyId2);

    List<FragranceInfo> searchSimilarFragranceOr(SimilarFragranceOrCond cond);

    List<FragranceInfo> searchSimilarFragrance(Long fragranceId, Long familyId);

    List<Long> extractByMainFamily(String mainFamilyCond, List<Long> fragranceIdList);

    List<FamilyCountDto> countFamily(List<Long> fragranceIdList);

    List<ExtractFragrance> extractFragrance(List<Long> familyIdList);
}
