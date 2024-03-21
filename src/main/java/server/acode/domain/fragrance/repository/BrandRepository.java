package server.acode.domain.fragrance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.acode.domain.fragrance.entity.Brand;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>, BrandRepositoryCustom {
    Optional<Brand> findByKorName(String korName);

    boolean existsByKorName(String brandName);
}
