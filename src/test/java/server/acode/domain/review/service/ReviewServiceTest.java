package server.acode.domain.review.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.fragrance.repository.FragranceRepository;
import server.acode.domain.review.entity.ReviewSeason;
import server.acode.domain.review.repository.ReviewSeasonRepository;

import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ReviewServiceTest {

    @Autowired ReviewService reviewService;
    @Autowired FragranceRepository fragranceRepository;
    @Autowired ReviewSeasonRepository reviewSeasonRepository;

    @DisplayName("리뷰 작성/ 삭제가 여러 스레드에서 일어날 때 reviewCnt 일관성 유지 테스트")
    @Test
    void reviewTest() throws InterruptedException {
        //given
        // 동시성 테스트 준비
        int numberOfThreads = 2;

        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        //when
        service.execute(() -> {
            try {
                reviewService.deleteCustomerReview(1L, 1L);
            } catch (ObjectOptimisticLockingFailureException e) {

            } finally {
                latch.countDown();
            }
        });
        service.execute(() -> {
            try {
                reviewService.deleteCustomerReview(2L, 2L);
            } catch (ObjectOptimisticLockingFailureException e) {

            } finally {
                latch.countDown();
            }
        });

        latch.await(); // 모든 쓰레드의 작업이 완료될 때까지 대기

        //then
        Fragrance fragrance = fragranceRepository.findById(1L).get();
        ReviewSeason season = reviewSeasonRepository.findByFragrance(fragrance).get();

        if(season.getVersion() == 1){
            // 하나의 스레드가 락에 걸린 경우
            assertThat(fragrance.getReviewCnt()).isEqualTo(1);
            assertThat(season.getSpring()).isEqualTo(1);
        } else if(season.getVersion() == 2) {
            // 두 스레드 모두 정상적으로 실행된 경우
            assertThat(fragrance.getReviewCnt()).isEqualTo(0);
            assertThat(fragrance.getRateSum()).isEqualTo(0);
            assertThat(season.getSpring()).isEqualTo(0);
        } else {
            fail("Unexpected version: " + season.getVersion());
        }

    }
}