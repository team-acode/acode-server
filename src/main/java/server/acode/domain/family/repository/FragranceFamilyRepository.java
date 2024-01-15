package server.acode.domain.family.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.acode.domain.family.entity.Family;
import server.acode.domain.family.entity.FragranceFamily;
import server.acode.domain.fragrance.entity.Fragrance;

import java.util.List;

@Repository
public interface FragranceFamilyRepository extends JpaRepository<FragranceFamily, Long>, FragranceFamilyRepositoryCustom {
    List<Family> findByFragrance(Fragrance fragrance);
}