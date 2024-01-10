package server.acode.domain.fragrance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import server.acode.domain.fragrance.entity.Capacity;
import server.acode.domain.fragrance.entity.Fragrance;

import java.util.List;
import java.util.Optional;

@Repository
public interface CapacityRepository extends JpaRepository<Capacity, Long> {
    List<Capacity> findByFragrance(Fragrance fragrance);
}
