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
import server.acode.domain.ingredient.repository.IngredientRepository;
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
    private final FragranceFamilyRepository fragranceFamilyRepository;
    private final FragranceRepository fragranceRepository;


    public PageableResponse searchFragranceByBrandAndFamily(FragranceFilterCond cond, PageRequest pageRequest){
        Pageable pageable = pageRequest.of();
        cond = parseFilterCond(cond);

        Page<FragranceCatalogDto> result = fragranceFamilyRepository.searchByBrandAndFamily(cond, pageable); // 향수 조회
        return new PageableResponse(result.getContent(), result.getTotalPages(), result.getTotalElements());
    }

    private FragranceFilterCond parseFilterCond(FragranceFilterCond cond){
        if(hasText(cond.getFamily())) {
            String[] parts = cond.getFamily().split("\\s+", 2);
            cond.setFamily(parts.length > 0 ? parts[0] : null);
            cond.setAdditionalFamily(parts.length > 1 ? parts[1] : null);
        }

        return cond;
    }

    public PageableResponse searchFragranceByIngredient(String ingredient, PageRequest pageRequest){
        Pageable pageable = pageRequest.of(); // pageable 객체로 변환

        Page<FragranceCatalogDto> result = fragranceRepository.searchByIngredient(ingredient, pageable);
        return new PageableResponse(result.getContent(), result.getTotalPages(), result.getTotalElements());
    }

    public BrandDetailsDto getBrandContent(String brandName){
        Brand find = brandRepository.findByKorName(brandName)
                .orElseThrow(() -> new CustomException(ErrorCode.BRAND_NOT_FOUND));

        return BrandDetailsDto.from(find);
    }

    public FamilyDetailsDto getFamilyContent(String family) {
        Family find = familyRepository.findByKorName(family)
                .orElseThrow(() -> new CustomException(ErrorCode.FAMILY_NOT_FOUND));

        return FamilyDetailsDto.from(find);
    }

    public IngredientDetailsDto getIngredientContent(String ingredient) {
        Ingredient findIngredient = ingredientRepository.findByKorName(ingredient)
                .orElseThrow(() -> new CustomException(ErrorCode.INGREDIENT_NOT_FOUND));

        return IngredientDetailsDto.from(findIngredient);
    }
}
