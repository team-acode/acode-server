package server.acode.domain.family.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import server.acode.domain.family.dto.request.FragranceCategoryCond;
import server.acode.domain.family.dto.request.FragranceSearchCond;
import server.acode.domain.family.dto.response.DisplayFragrance;
import server.acode.domain.family.dto.response.HomeFragrance;

import java.util.List;

@Repository
public interface FamilyRepositoryCustom {
    List<HomeFragrance> search(String familyCond);
    Page<DisplayFragrance> searchByCategory(FragranceCategoryCond cond, Pageable pageable);
}
