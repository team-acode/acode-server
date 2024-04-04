package server.acode.domain.fragrance.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.fragrance.repository.FragranceRepository;
import server.acode.global.common.ErrorCode;
import server.acode.global.exception.CustomException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@Transactional
class FragranceServiceTest {
    @Autowired
    SynchronizedFragranceService synchronizedFragranceService;
    @Autowired
    FragranceRepository fragranceRepository;
    @Autowired
    EntityManager em;


    @Test
    void 조회수_동시성() throws InterruptedException {
        //given
        //동시성 테스트 준비
        int numberOfThreads = 10;

        // 스레드 풀 생성
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        // CountDownLatch 생성 - 1개 혹은 그 이상의 스레드가 다른 스레드의 작업이 완료될 때까지 기다릴 수 있게 도와주는 클래스
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        Fragrance fragrance = fragranceRepository.findById(2L).orElseThrow(() -> new CustomException(ErrorCode.FRAGRANCE_NOT_FOUND));
        int initView = fragrance.getView();
        System.out.println("초기 조회수");
        System.out.println("fragrance 객체 : " + fragrance);
        System.out.println("initView : " + initView);

        em.flush();
        em.clear();

//        int initView = 13;

        //when
        for (int i = 0; i < numberOfThreads; i++) {
            System.out.println(i + "번째 호출");
            service.execute(() -> {
                try {
                    synchronizedFragranceService.synchronizedGetFragranceDetail(2L, null);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        // 모든 스레드의 작업이 완료될 때까지 대기
        latch.await();

        em.flush();
        em.clear();

        //then
        Fragrance fragrance2 = fragranceRepository.findById(2L).orElseThrow(() -> new CustomException(ErrorCode.FRAGRANCE_NOT_FOUND));
        System.out.println("fragrance2 : " + fragrance2);
        System.out.println("fragrance2.getView() : " + fragrance2.getView());
        System.out.println("initView : " + initView);
        Assertions.assertThat(fragrance2.getView()).isEqualTo(initView + numberOfThreads);
    }

}