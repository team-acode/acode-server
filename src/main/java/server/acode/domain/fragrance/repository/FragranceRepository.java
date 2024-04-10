package server.acode.domain.fragrance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import server.acode.domain.fragrance.entity.Fragrance;

@Repository
public interface FragranceRepository extends JpaRepository<Fragrance, Long>, FragranceRepositoryCustom {
    @Modifying
    @Query("UPDATE Fragrance f SET f.reviewCnt = f.reviewCnt + :cnt, f.rateSum = f.rateSum + :rate WHERE f.id = :fragranceId")
    void updateFragranceForReview(@Param("fragranceId") Long fragranceId,
                                  @Param("rate") int rate,
                                  @Param("cnt") int cnt);
    @Modifying
    @Query("UPDATE Fragrance f SET f.view = f.view + 1 WHERE f.id = :fragranceId")
    void updateFragranceView(@Param("fragranceId") Long fragranceId);

    @Modifying
    @Query("UPDATE Fragrance f " +
            "SET f.reviewCnt = f.reviewCnt + 1, " +
            "f.rateSum = f.rateSum + :rate " +
            "WHERE f.id = :fragranceId")
    void increaseReview(@Param("rate") int rate, @Param("fragranceId") Long fragranceId);

    @Modifying
    @Query("UPDATE Fragrance f " +
            "SET f.reviewCnt = f.reviewCnt - 1, " +
            "f.rateSum = f.rateSum - :rate " +
            "WHERE f.id = :fragranceId")
    void decreaseReview(@Param("rate") int rate, @Param("fragranceId") Long fragranceId);
}
