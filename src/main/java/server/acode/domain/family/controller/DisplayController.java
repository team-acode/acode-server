package server.acode.domain.family.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.util.StringUtils.*;
import org.springframework.web.bind.annotation.RestController;
import server.acode.domain.family.dto.request.FragranceFilterCond;
import server.acode.domain.family.dto.response.BrandDetailsDto;
import server.acode.domain.family.dto.response.FamilyDetailsDto;
import server.acode.domain.family.dto.response.IngredientDetailsDto;
import server.acode.domain.family.dto.response.PageableResponse;
import server.acode.domain.family.service.DisplayService;
import server.acode.global.common.PageRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "브랜드 상세/ 계열 상세/ 추천향료 상세", description = "브랜드 상세/ 계열 상세/ 추천향료 상세 API")
public class DisplayController {

    private final DisplayService displayService;

    @Operation(summary = "브랜드별/ 계열별/ 추천향료별 향수리스트",
            description = "필요한 값만 한글이름으로 파라미터에 넣으면 됩니다. "  +
                    "계열 두 개 검색 시에는 두 계열 사이 공백 한 칸 (url 상으로는 %20) 넣어주세요. " +
                    "페이지는 파라미터 없을 시 기본 page = 1, size = 10입니다.")
    @GetMapping("/display")
    public PageableResponse displayFragranceV1(FragranceFilterCond cond, PageRequest pageRequest){

        // param에 따라 계열별/브랜드별 or 추천향료별 분기
        PageableResponse response = hasText(cond.getIngredient())
                ? displayService.searchFragranceByIngredient(cond.getIngredient(), pageRequest)
                : displayService.searchFragranceByBrandAndFamily(cond, pageRequest);

        return response;
    }

    @Operation(summary = "브랜드 설명")
    @GetMapping("/display/brand/{brand}")
    public BrandDetailsDto displayBrandV1(@PathVariable("brand") String brand){
        return displayService.getBrandContent(brand);
    }

    @Operation(summary = "계열 설명")
    @GetMapping("/display/family/{family}")
    public FamilyDetailsDto displayFamilyV1(@PathVariable("family") String family){
        return displayService.getFamilyContent(family);
    }

    @Operation(summary = "향료 설명")
    @GetMapping("/display/ingredient/{ingredient}")
    public IngredientDetailsDto displayIngredientV1(@PathVariable("ingredient") String ingredient){
        return displayService.getIngredientContent(ingredient);
    }

}
