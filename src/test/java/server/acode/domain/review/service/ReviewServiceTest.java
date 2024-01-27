package server.acode.domain.review.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.fragrance.repository.FragranceRepository;
import server.acode.domain.review.dto.request.RegisterReviewRequest;
import server.acode.domain.user.entity.User;
import server.acode.global.exception.CustomException;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;
@SpringBootTest
@Transactional
class ReviewServiceTest {

    @Autowired ReviewService reviewService;
    @Autowired FragranceRepository fragranceRepository;

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
            RegisterReviewRequest request = new RegisterReviewRequest(3, "very good", "SPRING", "ONEHOUR", "WEAK", "CHIC");
            reviewService.registerReview(1L, request,1L);
            latch.countDown();

        });
        service.execute(() -> {
            reviewService.deleteReview(3L, 1L);
            latch.countDown();
        });


        latch.await(); // 모든 쓰레드의 작업이 완료될 때까지 대기

        //then
        Fragrance fragrance = fragranceRepository.findById(1L).get();
        assertThat(fragrance.getReviewCnt()).isEqualTo(1);

    }
}