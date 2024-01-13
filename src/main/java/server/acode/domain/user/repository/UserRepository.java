package server.acode.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import server.acode.domain.user.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAuthKey(String authKey);

    boolean existsByAuthKey(String authKey);

    @Modifying
    @Query("UPDATE User u SET u.reviewCnt = u.reviewCnt + 1 WHERE u.id = :userId")
    void increaseReviewCnt(@Param("userId") Long userId);
}
