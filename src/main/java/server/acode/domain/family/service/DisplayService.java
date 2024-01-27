package server.acode.domain.family.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.family.dto.request.FragranceFilterCond;
import server.acode.domain.family.dto.response.*;
import server.acode.domain.family.entity.Family;
import server.acode.domain.family.repository.FamilyRepository;
import server.acode.domain.family.repository.FragranceFamilyRepository;
import server.acode.domain.fragrance.entity.Brand;
import server.acode.domain.fragrance.repository.BrandRepository;
import server.acode.domain.fragrance.repository.FragranceRepository;
import server.acode.domain.ingredient.entity.Ingredient;
import server.acode.domain.ingredient.entity.IngredientType;
import server.acode.domain.ingredient.repository.IngredientRepository;
import server.acode.domain.ingredient.repository.IngredientTypeRepository;
import server.acode.global.common.ErrorCode;
import server.acode.global.common.PageRequest;
import server.acode.global.exception.CustomException;


import static org.springframework.util.StringUtils.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DisplayService {

    private final FamilyRepository familyRepository;
    private final BrandRepository brandRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientTypeRepository ingredientTypeRepository;
    private final FragranceFamilyRepository fragranceFamilyRepository;
    private final FragranceRepository fragranceRepository;


    public PageableResponse searchFragranceList(FragranceFilterCond cond, PageRequest pageRequest){
        Pageable pageable = pageRequest.of();

        String additionalFamily = null; // 추가 계열 변수 초기화
        // family에 값이 들어온 경우 값 처리
        if(hasText(cond.getFamily())) {
            String[] parts = cond.getFamily().split("\\s+", 2);
            cond.setFamily(parts.length > 0 ? parts[0] : null);
            additionalFamily = parts.length > 1 ? parts[1] : null;
        }

        Page<DisplayFragrance> result = fragranceFamilyRepository.searchByFilter(cond, additionalFamily, pageable); // 향수 조회
        return new PageableResponse(result.getContent(), result.getTotalPages(), result.getTotalElements());

    }

    public PageableResponse searchFragranceListByIngredient(String ingredient, PageRequest pageRequest){
        Pageable pageable = pageRequest.of(); // pageable 객체로 변환

        Page<DisplayFragrance> result = fragranceRepository.searchByIngredient(ingredient, pageable);
        return new PageableResponse(result.getContent(), result.getTotalPages(), result.getTotalElements());

    }

    public DisplayBrand getBrandContent(String brandName){
        // 존재하는지 확인 후 조회
        if(!brandRepository.existsByKorName(brandName)) throw new CustomException(ErrorCode.BRAND_NOT_FOUND);
        Brand find = brandRepository.findByKorName(brandName);

        return DisplayBrand.from(find);
    }

    public DisplayFamily getFamilyContent(String family) {
        // 존재하는지 확인 후 조회
        if(!familyRepository.existsByKorName(family)) throw new CustomException(ErrorCode.FAMILY_NOT_FOUND);
        Family find = familyRepository.findByKorName(family);

        return DisplayFamily.from(find);
    }

    public DisplayIngredient getIngredientContent(String ingredient) {
        // 향료가 존재하는지 확인 후 조회
        if(!ingredientRepository.existsByKorName(ingredient)) throw new CustomException(ErrorCode.INGREDIENT_NOT_FOUND);
        Ingredient findIngredient = ingredientRepository.findByKorName(ingredient);

        // 향료에 해당하는 향료 타입이 존재하는지 확인 후 조회
        IngredientType findIngredientType = ingredientTypeRepository.findById(findIngredient.getIngredientType().getId())
                .orElseThrow(()-> new CustomException(ErrorCode.INGREDIENT_TYPE_NOT_FOUND));

        return DisplayIngredient.from(findIngredient, findIngredientType);
    }
}
