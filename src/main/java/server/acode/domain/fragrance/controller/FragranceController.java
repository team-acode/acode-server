package server.acode.domain.fragrance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.acode.domain.fragrance.dto.response.*;
import server.acode.domain.fragrance.service.FragranceService;
import server.acode.global.auth.security.CustomUserDetails;
import server.acode.global.common.PageRequest;

@RestController
@RequestMapping("/api/v1/fragrance/{fragranceId}")
@RequiredArgsConstructor
@Tag(name = "Fragrance", description = "향수 상세 API")
public class FragranceController {
    private final FragranceService fragranceService;


    @Operation(summary = "기본 정보")
    @GetMapping
    public GetFragranceResponse getFragranceDetail(
            @PathVariable("fragranceId") Long fragranceId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return fragranceService.getFragranceDetail(fragranceId, userDetails);
    }


    @Operation(summary = "노트 정보",
            description = "\"single\" 필드 `true`이면 싱글노트입니다. \"topNote\"만 확인하면 됩니다. ")
    @GetMapping("/note")
    public GetFragranceNote getFragranceNote(@PathVariable("fragranceId") Long fragranceId) {
        return fragranceService.getFragranceNote(fragranceId);
    }


    @Operation(summary = "리뷰 미리보기")
    @GetMapping("/review/preview")
    public GetFragranceReviewPreview getFragranceReviewPreview(@PathVariable("fragranceId") Long fragranceId) {
        return fragranceService.getFragranceReviewPreview(fragranceId);
    }


    @GetMapping("/review/statistics")
    public GetFragranceReviewStatistics getFragranceReviewStatistics(@PathVariable("fragranceId") Long fragranceId) {
        return fragranceService.getFragranceReviewStatistics(fragranceId);
    }


    @Operation(summary = "유사 향수")
    @GetMapping("/similar")
    public GetFragranceSimilar getFragranceSimilar(@PathVariable("fragranceId") Long fragranceId) {
        return fragranceService.getFragranceSimilar(fragranceId);
    }


    @Operation(summary = "구매 링크")
    @GetMapping("/purchase")
    public GetFragrancePurchase getFragrancePurchase(@PathVariable("fragranceId") Long fragranceId) {
        return fragranceService.getFragrancePurchase(fragranceId);
    }


    @Operation(summary = "리뷰 더보기",
            description = "페이지 적용 해두었습니다\n\n" +
                    "필요한 값만 파라미터에 넣으면 됩니다\n\n" +
                    "페이지 관련 파라미터 없을 시 기본 page = 1, size = 10입니다")
    @GetMapping("/review")
    public GetFragranceReview getFragranceReview(@PathVariable("fragranceId") Long fragranceId, PageRequest pageRequest) {
        return fragranceService.getFragranceReview(fragranceId, pageRequest);
    }


    @Operation(summary = "스크랩 또는 스크랩 취소")
    @PostMapping("/scrap")
    public ResponseEntity<?> scrap(
            @PathVariable("fragranceId") Long fragranceId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return fragranceService.scrap(fragranceId, userDetails);
    }
}
