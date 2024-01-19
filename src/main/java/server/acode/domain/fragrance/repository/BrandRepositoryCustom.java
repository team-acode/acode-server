package server.acode.domain.fragrance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import server.acode.domain.fragrance.dto.response.BrandInfo;

@Repository
public interface BrandRepositoryCustom {
    Page<BrandInfo> searchBrand(String search, Pageable pageable);
}
