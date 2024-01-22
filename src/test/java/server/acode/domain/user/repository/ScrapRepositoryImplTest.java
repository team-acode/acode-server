package server.acode.domain.user.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.fragrance.entity.Brand;
import server.acode.domain.fragrance.entity.Concentration;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.fragrance.repository.FragranceRepository;
import server.acode.domain.user.dto.response.DisplayScrap;
import server.acode.domain.user.entity.Scrap;
import server.acode.domain.user.entity.User;


import static org.assertj.core.api.Assertions.*;
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
        //given
        User user = User.builder().nickname("testnickname").authKey("testauthkey").build();
        em.persist(user);

        Brand brand = Brand.builder().korName("testBrand").build();
        em.persist(brand);

        Fragrance testFragrance1 = Fragrance.builder()
                .name("testFragrance1")
                .thumbnail("testthumbnail1")
                .concentration(Concentration.EDC)
                .brand(brand)
                .build();

        Fragrance testFragrance2 = Fragrance.builder()
                .name("testFragrance2")
                .thumbnail("testthumbnail2")
                .concentration(Concentration.EDC)
                .brand(brand)
                .build();
        em.persist(testFragrance1);
        em.persist(testFragrance2);

        Scrap scrap = new Scrap(user, testFragrance2);
        em.persist(scrap);

        // when
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<DisplayScrap> results = scrapRepository.getScrap(user.getId(), pageRequest);

        //then
        assertThat(results.getTotalElements()).isEqualTo(1);
        assertThat(results.getTotalPages()).isEqualTo(1);
        assertThat(results.getContent()).extracting("fragranceId").containsExactly(testFragrance2.getId());

    }
}