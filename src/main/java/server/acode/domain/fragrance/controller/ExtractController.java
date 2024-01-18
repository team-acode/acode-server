package server.acode.domain.fragrance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.acode.domain.fragrance.dto.request.KeywordCond;
import server.acode.domain.fragrance.service.ExtractService;

@RestController
@RequestMapping("/api/v1/extract")
@RequiredArgsConstructor
@Tag(name = "Extract", description = "매칭 테스트 API")
public class ExtractController {
    private final ExtractService extractService;

    @Operation(summary = "매칭 테스트",
            description = "아직 미완성 입니다")    
    @GetMapping // 나중에 POST로ㅎ
    public ResponseEntity<?> extractFamily(@RequestBody KeywordCond cond) {
        return extractService.extractFamily(cond);
    }
}
