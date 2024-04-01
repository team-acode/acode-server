package server.acode.domain.review.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.fragrance.repository.FragranceRepository;
import server.acode.domain.review.entity.ReviewSeason;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReviewUpdateRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired ReviewUpdateRepository reviewUpdateRepository;
    @Autowired ReviewSeasonRepository reviewSeasonRepository;
    @Autowired FragranceRepository fragranceRepository;

    @Test
    public void updateTest(){
        // given
        Fragrance fragrance = fragranceRepository.findById(1L).get();

        // when
        reviewUpdateRepository.updateSeason("spring", 1L, 1);
        em.flush();
        em.clear();

        // then
        Optional<ReviewSeason> reviewSeason = reviewSeasonRepository.findByFragrance(fragrance);
        assertThat(reviewSeason.get().getSpring()).isEqualTo(3);
    }
}