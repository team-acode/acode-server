package server.acode.domain.fragrance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.family.entity.Family;
import server.acode.domain.family.repository.FamilyRepository;
import server.acode.domain.family.repository.FragranceFamilyRepository;
import server.acode.domain.fragrance.dto.request.KeywordCond;
import server.acode.domain.fragrance.dto.response.ExtractFamily;
import server.acode.domain.fragrance.dto.response.ExtractFragrance;
import server.acode.domain.fragrance.dto.response.ExtractResponse;
import server.acode.domain.fragrance.dto.response.FamilyCountDto;
import server.acode.domain.fragrance.repository.FragranceRepository;

import java.util.Random;
import java.util.stream.Collectors;

import java.util.List;

import static org.springframework.util.StringUtils.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExtractService {
    private final FragranceRepository fragranceRepository;
    private final FragranceFamilyRepository fragranceFamilyRepository;
    private final FamilyRepository familyRepository;

    public ExtractResponse extractFamily(KeywordCond cond) {
        List<Long> fragranceIdList = extractFragranceIdList(cond);
        System.out.println("\nfragranceIdList = " + fragranceIdList + "\n");

        List<FamilyCountDto> familyCountDtoList = fragranceFamilyRepository.countFamily(fragranceIdList);
        for (FamilyCountDto familyCountDto : familyCountDtoList) {
            System.out.println("familyCountDto = " + familyCountDto);
        }

        List<Long> familyIdList = familyCountDtoList.stream()
                .filter(familyCountDto -> familyCountDto.getCount().equals(familyCountDtoList.get(0).getCount()))
                .map(FamilyCountDto::getFamilyId)
                .toList();

        System.out.println("\n");
        for (Long l : familyIdList) {
            System.out.println("familyId = " + l);
        }

        // 계열이 세 개 이상
        if (familyIdList.size() > 3) {
            Random random = new Random();
            List<Long> customFamilyIdList = random.ints(0, familyIdList.size())
                    .distinct()
                    .limit(3)
                    .mapToObj(familyIdList::get)
                    .collect(Collectors.toList());

            return getExtractResult(customFamilyIdList);

        }

        return getExtractResult(familyIdList);

    }


    private ExtractResponse getExtractResult (List<Long> familyIdList){
        List<Family> families = familyRepository.findByIdIn(familyIdList);
        List<ExtractFamily> extractFamilies = families.stream()
                .map(ExtractFamily::from)
                .collect(Collectors.toList());

        List<ExtractFragrance> fragrances = fragranceFamilyRepository.extractFragrance(familyIdList);

        // 향수가 세 개 이상인 경우
        if (fragrances.size() > 5) {
            Random random = new Random();
            List<ExtractFragrance> collect = random.ints(0, fragrances.size())
                    .distinct()
                    .limit(5)
                    .mapToObj(fragrances::get)
                    .collect(Collectors.toList());

            return new ExtractResponse(extractFamilies, collect);
        }

        return new ExtractResponse(extractFamilies, fragrances);
    }



    private List<Long> extractFragranceIdList(KeywordCond cond) {
        List<Long> fragranceIdList1;
        List<Long> fragranceIdList2;



        //1번
        if (cond.getConcentration().size() == 2) {
            fragranceIdList1 = fragranceRepository.extractByConcentrationOr(cond.getConcentration().get(0), cond.getConcentration().get(1));
            System.out.println("\nQ1 2 keywords just AND : " + cond.getConcentration().get(0) + " & " + cond.getConcentration().get(1));
            System.out.println("fragranceIdList1 : " + fragranceIdList1);
        } else {
            fragranceIdList1 = fragranceRepository.extractByConcentration(cond.getConcentration().get(0), "");
            System.out.println("\nQ1 1 keyword : " + cond.getConcentration().get(0));
            System.out.println("fragranceIdList1 : " + fragranceIdList1);
        }



        //2번
        if (cond.getSeason().size() == 2) {
            fragranceIdList2 = fragranceRepository.extractBySeason(cond.getSeason().get(0), cond.getSeason().get(1), fragranceIdList1);
            System.out.println("\nQ2 2 keywords AND : " + cond.getSeason().get(0) + " & " + cond.getSeason().get(1));
            System.out.println("fragranceIdList2 : " + fragranceIdList2);

            if (fragranceIdList2 == null || fragranceIdList2.isEmpty()) {
                fragranceIdList2 = fragranceRepository.extractBySeasonOr(cond.getSeason().get(0), cond.getSeason().get(1), fragranceIdList1);
                System.out.println("Q2 2 keywords OR : " + cond.getSeason().get(0) + " & " + cond.getSeason().get(1));
                System.out.println("fragranceIdList2 : " + fragranceIdList2);
            }
        } else {
            fragranceIdList2 = fragranceRepository.extractBySeason(cond.getSeason().get(0), "", fragranceIdList1);
            System.out.println("\nQ2 1 keyword : " + cond.getSeason().get(0));
            System.out.println("fragranceIdList2 : " + fragranceIdList2);
        }



        if (fragranceIdList2.isEmpty()) {
            System.out.println("\nNO RESULTS UNTIL Q2 -> RETURN Q1");
            return fragranceIdList1;
        }



        // 3번 질문
        if (cond.getMainFamily().size() == 2) {
            fragranceIdList1 = fragranceFamilyRepository.extractByMainFamily(cond.getMainFamily().get(0), cond.getMainFamily().get(1), fragranceIdList2);
            System.out.println("\nQ3 2 keywords AND : " + cond.getMainFamily().get(0) + " & " + cond.getMainFamily().get(1));
            System.out.println("fragranceIdList1 : " + fragranceIdList1);

            if (fragranceIdList1.isEmpty()) {
                fragranceIdList1 = fragranceFamilyRepository.extractByMainFamilyOr(cond.getMainFamily().get(0), cond.getMainFamily().get(1), fragranceIdList2);
                System.out.println("Q3 2 keywords OR : " + cond.getMainFamily().get(0) + " & " + cond.getMainFamily().get(1));
                System.out.println("fragranceIdList1 : " + fragranceIdList1);
            }
        } else {
            fragranceIdList1 = fragranceFamilyRepository.extractByMainFamily(cond.getMainFamily().get(0), "", fragranceIdList2);
            System.out.println("\nQ3 1 keyword : " + cond.getMainFamily().get(0));
            System.out.println("fragranceIdList1 : " + fragranceIdList1);
        }



        if (fragranceIdList1.isEmpty()) {
            System.out.println("\nNO RESULTS UNTIL Q3 -> RETURN Q2");
            return fragranceIdList2;
        }



        // 4번 질문
        if (cond.getScent().size() == 2) { // 향 2개 AND
            fragranceIdList2 = fragranceRepository.extractByScent(cond.getScent().get(0), cond.getScent().get(1), fragranceIdList1);
            System.out.println("\nQ4 2 keywords AND : " + cond.getScent().get(0) + " & " + cond.getScent().get(1));
            System.out.println("fragranceIdList2 : " + fragranceIdList2);

            if (fragranceIdList2.isEmpty()) { // 향 2개 OR
                fragranceIdList2 = fragranceRepository.extractByScentOr(cond.getScent().get(0), cond.getScent().get(1), fragranceIdList1);
                System.out.println("\nQ4 2 keyword OR : " + cond.getScent().get(0) + " & " + cond.getScent().get(1));
                System.out.println("fragranceIdList2 : " + fragranceIdList2);
            }

        } else { // 향 1개
            fragranceIdList2 = fragranceRepository.extractByScent(cond.getScent().get(0), "", fragranceIdList1);
            System.out.println("\nQ4 1 keyword : " + cond.getScent().get(0));
            System.out.println("fragranceIdList2 : " + fragranceIdList2);
        }



        if (fragranceIdList2.isEmpty()) {
            System.out.println("\nNO RESULTS UNTIL Q4 -> RETURN Q3");
            return fragranceIdList1;
        }



        // 5번 질문
        if (cond.getScent().size() == 2) { // 스타일 2개 AND
            fragranceIdList1 = fragranceRepository.extractByStyle(cond.getStyle().get(0), cond.getStyle().get(1), fragranceIdList2);
            System.out.println("\nQ5 2 keywords AND : " + cond.getStyle().get(0) + " & " + cond.getStyle().get(1));
            System.out.println("fragranceIdList1 : " + fragranceIdList1);

            if (fragranceIdList1.isEmpty()) { // 스타일 2개 OR
                fragranceIdList1 = fragranceRepository.extractByStyleOr(cond.getStyle().get(0), cond.getStyle().get(1), fragranceIdList2);
                System.out.println("Q5 2 keywords OR : " + cond.getStyle().get(0) + " & " + cond.getStyle().get(1));
                System.out.println("fragranceIdList1 : " + fragranceIdList1);
            }
        } else { // 스타일 1개
            fragranceIdList1 = fragranceRepository.extractByStyle(cond.getStyle().get(0), "", fragranceIdList2);
            System.out.println("\nQ5 1 keyword : " + cond.getScent().get(0));
            System.out.println("fragranceIdList1 : " + fragranceIdList1);
        }



        if (fragranceIdList1.isEmpty()) {
            System.out.println("\nNO RESULTS UNTIL Q5 -> RETURN Q4");
            return fragranceIdList2;
        }



        System.out.println("\nRETURN Q5");
        return fragranceIdList1;
    }
}
