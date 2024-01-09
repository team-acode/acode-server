package server.acode.domain.fragrance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.acode.domain.fragrance.entity.Fragrance;

@Repository
public interface FragranceRepository extends JpaRepository<Fragrance, Long> {
}
