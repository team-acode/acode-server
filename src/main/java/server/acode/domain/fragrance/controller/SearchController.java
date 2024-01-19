package server.acode.domain.fragrance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.acode.domain.family.dto.response.PageableResponse;
import server.acode.domain.fragrance.dto.request.SearchCond;
import server.acode.domain.fragrance.dto.response.FragranceInfo;
import server.acode.domain.fragrance.dto.response.SearchBrandResponse;
import server.acode.domain.fragrance.service.SearchService;
import server.acode.global.common.PageRequest;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "검색 API")
public class SearchController {
    private final SearchService searchService;

    @Operation(summary = "브랜드 검색",
            description = "search 검색어: 필수\n\n" +
                    "검색어 없는 경우 `400 SEARCH_NOT_FOUND`")
    @GetMapping("/brand")
    public SearchBrandResponse searchBrand(@RequestParam("search") String search) {
        return searchService.searchBrand(search);
    }

    @Operation(summary = "향수 검색",
            description = "검색어 외에는 필요한 값만 파라미터에 넣으면 됩니다 \n\n" +
                    "search 검색어: 필수\n\n" +
                    "  검색어 없는 경우 `400 SEARCH_NOT_FOUND`\n\n" +
                    "family 계열: 한글로 넣어주세요 ex. `플로럴`\n\n" +
                    "  계열 두 개 검색 시에는 두 계열 사이 공백 한 칸 (url 상으로는 %20) 넣어주세요 ex. `플로럴 프루티` \n\n" +
                    "페이지는 파라미터 없을 시 기본 page = 1, size = 10입니다")
    @GetMapping("/fragrance")
    public PageableResponse<FragranceInfo> searchFragrance(SearchCond cond, PageRequest pageRequest) {
        return searchService.searchFragrance(cond, pageRequest);
    }
}
