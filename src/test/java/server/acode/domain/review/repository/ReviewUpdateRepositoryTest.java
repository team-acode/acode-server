package server.acode.domain.review.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.review.entity.ReviewSeason;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
@SpringBootTest
@Transactional
class ReviewUpdateRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired ReviewUpdateRepository reviewUpdateRepository;
    @Autowired ReviewSeasonRepository reviewSeasonRepository;

    @Test
    public void updateTest(){
        // given
        Fragrance testFragrance1 = Fragrance.builder()
                .name("testFragrance1")
                .build();
        em.persist(testFragrance1);
        reviewSeasonRepository.insertStatistics(testFragrance1.getId()); // ReviewSeason 생성

        // when
        reviewUpdateRepository.updateSeason("spring", testFragrance1.getId(), 1);
        em.flush();
        em.clear();

        // then
        Optional<ReviewSeason> reviewSeason = reviewSeasonRepository.findByFragrance(testFragrance1);
        assertThat(reviewSeason.get().getSpring()).isEqualTo(1);
    }
}