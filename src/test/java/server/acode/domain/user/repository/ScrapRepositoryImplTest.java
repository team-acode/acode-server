package server.acode.domain.user.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.fragrance.repository.FragranceRepository;
import server.acode.domain.user.dto.response.DisplayScrap;
import server.acode.domain.user.entity.Role;
import server.acode.domain.user.entity.Scrap;
import server.acode.domain.user.entity.User;
import server.acode.global.common.PageRequest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Commit
class ScrapRepositoryImplTest {
    @Autowired
    EntityManager em;

    @Autowired ScrapRepository scrapRepository;
    @Autowired UserRepository userRepository;
    @Autowired FragranceRepository fragranceRepository;


    @Test
    public void getScrap() {
        // TODO 나중에 체크
        //given
        User user = new User(101L, Role.ROLE_USER, "test4", "nicknameTest");
        userRepository.save(user);
        System.out.println("user.getId() = " + user.getId());

        Fragrance testFragrance1 = Fragrance.builder()
                .name("testFragrance1")
                .id(100L)
                .build();

        Fragrance testFragrance2 = Fragrance.builder()
                .name("testFragrance2")
                .id(101L)
                .build();
        fragranceRepository.save(testFragrance1);
        System.out.println("testFragrance1 = " + testFragrance1);
        fragranceRepository.save(testFragrance2);
        System.out.println("testFragrance2 = " + testFragrance2);

        // when
        Scrap scrap = new Scrap(100L, user, testFragrance1);
        scrapRepository.save(scrap);
        System.out.println("scrap = " + scrap);
        Page<DisplayScrap> results = scrapRepository.getScrap(user.getId(), new PageRequest().of());
        System.out.println(results);

        // then
        assertThat(results.getContent().get(0).getFragranceId()).isEqualTo(testFragrance1.getId());
        assertThat(results.getTotalElements()).isEqualTo(1);
    }
}