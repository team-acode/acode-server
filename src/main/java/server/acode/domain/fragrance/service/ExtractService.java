package server.acode.domain.fragrance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        List<ExtractFamily> families = familyRepository.extractFamilies(familyIdList);
        List<ExtractFragrance> fragrances = fragranceFamilyRepository.extractFragrance(familyIdList);

        // 향수가 세 개 이상인 경우
        if(fragrances.size() > 5) {
            Random random = new Random();
            List<ExtractFragrance> collect = random.ints(0, fragrances.size())
                    .distinct()
                    .limit(5)
                    .mapToObj(fragrances::get)
                    .collect(Collectors.toList());

            return new ExtractResponse(families, collect);
        }

        return new ExtractResponse(families, fragrances);
    }



    private List<Long> extractFragranceIdList(KeywordCond cond) {
        // 1번, 2번 질문 - 반드시 결과 있음
        List<Long> fragranceIdList1 = fragranceRepository.extractByConcentrationAndSeason(cond.getConcentration(), cond.getSeason());
        System.out.println("\n1번 concentration, 2번 season");
        System.out.println("fragranceIdList1 : " + fragranceIdList1);

        // 3번 질문
        List<Long> fragranceIdList2 = fragranceFamilyRepository.extractByMainFamily(cond.getMainFamily(), fragranceIdList1);
        System.out.println("\n3번 mainFamily");

        if (fragranceIdList2 == null) {
            System.out.println("\n3번 질문 결과 없어서 1번2번 질문까지만");
            System.out.println("fragranceIdList1 = " + fragranceIdList1);
            return fragranceIdList1;
        }
        System.out.println("fragranceIdList2 = " + fragranceIdList2);

        // 4번 질문
        if (hasText(cond.getScent2())) { // 향 2개 AND
            fragranceIdList1 = fragranceRepository.extractByScent(cond.getScent1(), cond.getScent2(), fragranceIdList2);
            System.out.println("\n4번 scent AND");

            if (fragranceIdList1.isEmpty()) { // 향 2개 OR
                fragranceIdList1 = fragranceRepository.extractByScentOr(cond.getScent1(), cond.getScent2(), fragranceIdList2);
                System.out.println("4번 scent OR");
            }

        } else { // 향 1개
            fragranceIdList1 = fragranceRepository.extractByScent(cond.getScent1(), cond.getScent2(), fragranceIdList2);
            System.out.println("\n4번 scent 1개");
        }

        if (fragranceIdList1.isEmpty()) {
            System.out.println("\n4번 질문 결과 없어서 3번 질문까지만");
            System.out.println("fragranceIdList2 = " + fragranceIdList2);
            return fragranceIdList2;
        }
        System.out.println("fragranceIdList1 = " + fragranceIdList1);

        // 5번 질문
        if (hasText(cond.getStyle2())) { // 스타일 2개 AND
            fragranceIdList2 = fragranceRepository.extractByStyle(cond.getStyle1(), cond.getStyle2(), fragranceIdList1);
            System.out.println("\n5번 style AND");

            if (fragranceIdList2.isEmpty()) { // 스타일 2개 OR
                fragranceIdList2 = fragranceRepository.extractByStyleOr(cond.getStyle1(), cond.getStyle2(), fragranceIdList1);
                System.out.println("5번 style OR");
            }
        } else { // 스타일 1개
            fragranceIdList2 = fragranceRepository.extractByStyle(cond.getStyle1(), cond.getStyle2(), fragranceIdList1);
            System.out.println("\n5번 style 1개");
        }

        if (fragranceIdList2.isEmpty()) {
            System.out.println("\n5번 질문 결과 없어서 4번 질문까지만");
            System.out.println("fragranceIdList1 = " + fragranceIdList1);
            return fragranceIdList1;
        }

        System.out.println("fragranceIdList2 = " + fragranceIdList2);
        return fragranceIdList2;
    }
}
