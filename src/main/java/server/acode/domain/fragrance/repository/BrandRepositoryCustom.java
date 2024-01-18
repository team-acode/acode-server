package server.acode.domain.fragrance.repository;

import org.springframework.stereotype.Repository;
import server.acode.domain.fragrance.dto.response.BrandInfo;

import java.util.List;

@Repository
public interface BrandRepositoryCustom {
    List<BrandInfo> searchBrand(String search);
}
