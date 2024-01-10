package server.acode.domain.family.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FamilyRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired FamilyRepository familyRepository;

    @Test
    public void searchTest(){

    }

}