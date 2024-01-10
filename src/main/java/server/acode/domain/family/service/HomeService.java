package server.acode.domain.family.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.acode.domain.family.dto.request.FragranceSearchCond;
import server.acode.domain.family.dto.response.FragranceByCatgegory;
import server.acode.domain.family.repository.FamilyRepository;
import server.acode.domain.ingredient.dto.response.IngredientOfTheDay;
import server.acode.domain.ingredient.repository.IngredientRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final FamilyRepository familyRepository;
    private final IngredientRepository ingredientRepository;

    public List<FragranceByCatgegory> search(FragranceSearchCond condition){

        List<FragranceByCatgegory> result = familyRepository.search(condition); // 계열과 일치하는 향수 가져오기

        result.forEach(fragrance -> {
            String[] styles = fragrance.getStyle().split(", ");
            List<String> limitedStyles = Arrays.stream(styles)
                    .map(s -> "#" + s)
                    .limit(3) // 최대 세 개의 스타일만 유지
                    .collect(Collectors.toList());

            String modifiedStyle = String.join(", ", limitedStyles);
            fragrance.setStyle(modifiedStyle);
        });

        return result;
    }

    public IngredientOfTheDay recommendIngredient() {
        List<IngredientOfTheDay> result = ingredientRepository.getTodayIngreient();

        int index = LocalDate.now().getDayOfYear() % 5; // 오늘 날짜 기준으로 인덱스 생성

        return result.get(index);
    }
}
