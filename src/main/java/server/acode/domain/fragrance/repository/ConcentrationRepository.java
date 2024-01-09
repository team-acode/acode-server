package server.acode.domain.fragrance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.acode.domain.fragrance.entity.Concentration;

@Repository
public interface ConcentrationRepository extends JpaRepository<Concentration, Long> {
}
