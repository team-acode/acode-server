package server.acode.domain.family.repository;

import org.springframework.stereotype.Repository;
import server.acode.domain.family.dto.SimilarFragranceOrCond;
import server.acode.domain.fragrance.dto.response.FragranceInfo;

import java.util.List;

@Repository
public interface FragranceFamilyRepositoryCustom {
    List<Long> searchFamilyByFragranceId(Long fragranceId);
    List<FragranceInfo> searchSimilarFragranceAnd(Long fragranceId, Long familyId1, Long familyId2);

    List<FragranceInfo> searchSimilarFragranceOr(SimilarFragranceOrCond cond);
    List<FragranceInfo> searchSimilarFragrance(Long fragranceId, Long familyId);
}
