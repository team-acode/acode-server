package server.acode.domain.fragrance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.acode.domain.fragrance.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long>, BrandRepositoryCustom {
    public Brand findByKorName(String korName);

    boolean existsByKorName(String brandName);
}
