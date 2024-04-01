package server.acode.domain.user.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.user.entity.User;
import server.acode.domain.user.repository.UserRepository;
import server.acode.global.exception.CustomException;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired EntityManager em;
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @DisplayName("테스트 DB 분리")
    @Test
    void findAll(){
        List<User> findUsers = userRepository.findAll();


        assertThat(findUsers.size()).isEqualTo(2);
        assertThat(findUsers).extracting(User::getAuthKey)
                .contains("testauthkey1", "testauthkey2");

    }

    @DisplayName("동시에 같은 닉네임으로 변경 요청")
    @Test
    void updateNickname() throws InterruptedException {
        //given
        // 동시성 테스트 준비
        int numberOfThreads = 2;

        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        //when
        service.execute(() -> {
            try {
                userService.synchronizedUpdateNickname("same", 1L);
            } catch (CustomException e) {

            } finally {
                latch.countDown();
            }
        });
        service.execute(() -> {
            try {
                userService.synchronizedUpdateNickname("same", 2L);
            } catch (CustomException e) {

            } finally {
                latch.countDown();
            }
        });


        latch.await(); // 모든 쓰레드의 작업이 완료될 때까지 대기

        //then
        User user1 = userRepository.findById(1L).get();
        User user2 = userRepository.findById(2L).get();

        assertThat(user1.getNickname()).isNotEqualTo(user2.getNickname());
    }
}