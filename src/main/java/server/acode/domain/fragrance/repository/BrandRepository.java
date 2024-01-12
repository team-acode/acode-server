package server.acode.domain.fragrance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.acode.domain.fragrance.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    public Brand findByKorName(String korName);
}
