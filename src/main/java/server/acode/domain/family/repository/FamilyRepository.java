package server.acode.domain.family.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.acode.domain.family.entity.Family;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyRepository extends JpaRepository<Family, Long>{
    boolean existsByKorName(String korName);
    Optional<Family> findByKorName(String korName);

    List<Family> findByIdIn(List<Long> familyIdList);
}
