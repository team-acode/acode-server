package server.acode.domain.fragrance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.acode.domain.fragrance.dto.request.KeywordCond;
import server.acode.domain.fragrance.service.ExtractService;

@RestController
@RequestMapping("/api/v1/extract")
@RequiredArgsConstructor
public class ExtractController {
    private final ExtractService extractService;

    @GetMapping // 나중에 POST로ㅎ
    public ResponseEntity<?> extractFamily(@RequestBody KeywordCond cond) {
        return extractService.extractFamily(cond);
    }
}
