package server.acode.domain.family.repository;

import server.acode.domain.family.dto.request.FragranceSearchCond;
import server.acode.domain.family.dto.response.FragranceByCatgegory;

import java.util.List;

public interface FamilyRepositoryCustom {
    List<FragranceByCatgegory> search(FragranceSearchCond cond);
}
