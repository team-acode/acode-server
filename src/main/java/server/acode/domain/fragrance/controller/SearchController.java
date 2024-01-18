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

    @Operation(summary = "브랜드 검색")
    @GetMapping("/brand")
    public SearchBrandResponse searchBrand(@RequestParam("search") String search) {
        return searchService.searchBrand(search);
    }

    @Operation(summary = "향수 검색")
    @GetMapping("/fragrance")
    public PageableResponse<FragranceInfo> searchFragrance(SearchCond cond, PageRequest pageRequest) {
        return searchService.searchFragrance(cond, pageRequest);
    }
}
