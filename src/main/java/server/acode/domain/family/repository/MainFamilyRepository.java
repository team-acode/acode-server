package server.acode.domain.family.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.acode.domain.family.entity.MainFamily;

@Repository
public interface MainFamilyRepository extends JpaRepository<MainFamily, Long> {
}
