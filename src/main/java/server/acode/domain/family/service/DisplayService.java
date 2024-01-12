package server.acode.domain.family.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import server.acode.domain.family.dto.request.FragranceFilterCond;
import server.acode.domain.family.dto.response.DisplayFragrance;
import server.acode.domain.family.dto.response.DisplayResponse;
import server.acode.domain.family.repository.FamilyRepository;
import server.acode.domain.ingredient.entity.Ingredient;
import server.acode.domain.ingredient.repository.IngredientRepository;
import server.acode.global.common.PageRequest;

import static org.springframework.util.StringUtils.*;

@Service
@RequiredArgsConstructor
public class DisplayService {
    private final FamilyRepository familyRepository;
    private final IngredientRepository ingredientRepository;

    public DisplayResponse searchFragranceList(FragranceFilterCond cond, PageRequest pageRequest){
        Pageable pageable = pageRequest.of();

        String additionalFamily = null; // 추가 계열 변수 초기화
        // family에 값이 들어온 경우 값 처리
        if(hasText(cond.getFamily())) {
            String[] parts = cond.getFamily().split("\\s+", 2);
            cond.setFamily(parts.length > 0 ? parts[0] : null);
            additionalFamily = parts.length > 1 ? parts[1] : null;
        }

        Page<DisplayFragrance> result = familyRepository.searchByFilter(cond, additionalFamily, pageable); // 향수 조회
        return getDisplayResponse(result);
    }

    public DisplayResponse searchFragranceListByIngredient(String ingredient, PageRequest pageRequest){

        Pageable pageable = pageRequest.of(); // pageable 객체로 변환

        Page<DisplayFragrance> result = familyRepository.searchByIngredient(ingredient, pageable);
        return getDisplayResponse(result);

    }

    // displayResponseDto로 변환
    private DisplayResponse getDisplayResponse(Page<DisplayFragrance> data){
        DisplayResponse response = new DisplayResponse();
        response.setTotalPages(data.getTotalPages());
        response.setTotalElements(data.getTotalElements());
        response.setData(data.getContent());

        return response;
    }

}
