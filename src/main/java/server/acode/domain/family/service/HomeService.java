package server.acode.domain.family.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.family.dto.response.HomeFragranceDto;
import server.acode.domain.family.repository.FamilyRepository;
import server.acode.domain.family.repository.FragranceFamilyRepository;
import server.acode.domain.ingredient.dto.response.IngredientOfTheDay;
import server.acode.domain.ingredient.repository.IngredientRepository;
import server.acode.global.common.ErrorCode;
import server.acode.global.exception.CustomException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    private final FamilyRepository familyRepository;
    private final FragranceFamilyRepository fragranceFamilyRepository;
    private final IngredientRepository ingredientRepository;

    public List<HomeFragranceDto> searchMainFragrance(String familyName){

        if(!familyRepository.existsByKorName(familyName)) throw new CustomException(ErrorCode.FAMILY_NOT_FOUND);

        List<HomeFragranceDto> result = fragranceFamilyRepository.search(familyName); // 계열과 일치하는 향수 가져오기
        return result;
    }

    public IngredientOfTheDay recommendIngredient() {
        List<IngredientOfTheDay> ingredientOfTheDayGroup = ingredientRepository.getIngredientsOfTheDay();

        int order = LocalDate.now().getDayOfYear() % 5; // 오늘 날짜 기준으로 순서 확인
        return ingredientOfTheDayGroup.get(order); // 순서에 해당하는 향료 리턴
    }
}
