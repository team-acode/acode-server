package server.acode.domain.user.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.fragrance.entity.Brand;
import server.acode.domain.fragrance.entity.Concentration;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.fragrance.repository.FragranceRepository;
import server.acode.domain.user.dto.response.ScrapDto;
import server.acode.domain.user.entity.Scrap;
import server.acode.domain.user.entity.User;


import static org.assertj.core.api.Assertions.*;
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ScrapRepositoryImplTest {
    @Autowired
    EntityManager em;
    @Autowired ScrapRepository scrapRepository;
    @Autowired UserRepository userRepository;
    @Autowired FragranceRepository fragranceRepository;


    @Test
    public void getScrap() {
        //given
        User user = userRepository.findById(1L).get();
        Fragrance fragrance = fragranceRepository.findById(1L).get();

        Scrap scrap = new Scrap(user, fragrance);
        em.persist(scrap);

        // when
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ScrapDto> results = scrapRepository.getScrap(1L, pageRequest);

        //then
        assertThat(results.getTotalElements()).isEqualTo(1);
        assertThat(results.getTotalPages()).isEqualTo(1);
        assertThat(results.getContent()).extracting("fragranceId").containsExactly(1L);
    }
}