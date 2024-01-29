package server.acode.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.user.entity.Scrap;
import server.acode.domain.user.entity.User;

import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long>, ScrapRepositoryCustom {
    @Query("SELECT s FROM Scrap s WHERE s.user = :user AND s.fragrance = :fragrance")
    Optional<Scrap> findByUserAndFragrance(@Param("user") User user, @Param("fragrance") Fragrance fragrance);

    @Modifying
    void deleteByUserAndFragrance(User user, Fragrance fragrance);

    Optional<Scrap> findByUserId(Long userId);

}
