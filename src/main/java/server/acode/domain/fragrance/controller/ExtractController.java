package server.acode.domain.fragrance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.acode.domain.fragrance.dto.request.KeywordCond;
import server.acode.domain.fragrance.dto.response.ExtractResponse;
import server.acode.domain.fragrance.service.ExtractService;

@RestController
@RequestMapping("/api/v1/extract")
@RequiredArgsConstructor
@Tag(name = "Extract", description = "매칭 테스트 API")
public class ExtractController {
    private final ExtractService extractService;

    @Operation(summary = "매칭 테스트",
            description = "계열 최대 세 개, 추천 향수 최대 다섯 개(임의) 리턴됩니다")
    @PostMapping
    public ExtractResponse extractFamily(@RequestBody KeywordCond cond) {
        return extractService.extractFamily(cond);
    }
}
