package server.acode.domain.user.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.fragrance.repository.FragranceRepository;
import server.acode.domain.user.dto.response.DisplayScrap;
import server.acode.domain.user.entity.Role;
import server.acode.domain.user.entity.Scrap;
import server.acode.domain.user.entity.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
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
        User user = User.builder().nickname("testnickname").authKey("testauthkey").build();
        em.persist(user);

        Fragrance testFragrance1 = Fragrance.builder()
                .name("testFragrance1")
                .build();

        Fragrance testFragrance2 = Fragrance.builder()
                .name("testFragrance2")
                .build();
        em.persist(testFragrance1);
        em.persist(testFragrance2);

        // when
        Scrap scrap = new Scrap(user, testFragrance1);
        em.persist(scrap);

        //then
        Page<DisplayScrap> results = scrapRepository.getScrap(user.getId(), PageRequest.of(0, 10));
        List<Scrap> all = scrapRepository.findAll();
        System.out.println(all);
    }
}