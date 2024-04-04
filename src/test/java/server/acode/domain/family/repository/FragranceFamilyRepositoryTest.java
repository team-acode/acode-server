package server.acode.domain.family.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.fragrance.dto.response.FamilyCountDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FragranceFamilyRepositoryTest {
    @Autowired
    EntityManager em;

    @Autowired
    FragranceFamilyRepository fragranceFamilyRepository;

    @Test
    public void countFamilyTest() {
        //given
//        List<Long> fragranceIdList = List.of(4L, 6L, 15L, 20L, 37L, 44L, 51L);
        List<Long> fragranceIdList = List.of(10L, 24L, 29L, 35L, 47L);

        //when
        List<FamilyCountDto> familyCountDtoList = fragranceFamilyRepository.countFamily(fragranceIdList);
        for (FamilyCountDto familyCountDto : familyCountDtoList) {
            System.out.println("familyCountDto = " + familyCountDto);
        }

        List<FamilyCountDto> list = familyCountDtoList.stream()
                .filter(familyCountDto -> familyCountDto.getCount().equals(familyCountDtoList.get(0).getCount()))
                .toList();
        System.out.println("=====");
        for (FamilyCountDto familyCountDto : list) {
            System.out.println("familyCountDto = " + familyCountDto);
        }


        //then
    }
}