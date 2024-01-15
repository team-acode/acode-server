package server.acode.domain.fragrance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.acode.domain.fragrance.dto.response.*;
import server.acode.domain.fragrance.service.FragranceService;
import server.acode.global.auth.security.CustomUserDetails;
import server.acode.global.common.PageRequest;

@RestController
@RequestMapping("/api/v1/fragrance")
@RequiredArgsConstructor
public class FragranceController {
    private final FragranceService fragranceService;


    @GetMapping("/{fragranceId}")
    public GetFragranceResponse getFragranceDetail(
            @PathVariable("fragranceId") Long fragranceId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return fragranceService.getFragranceDetail(fragranceId, userDetails);
    }


    @GetMapping("/{fragranceId}/note")
    public GetFragranceNote getFragranceNote(@PathVariable("fragranceId") Long fragranceId) {
        return fragranceService.getFragranceNote(fragranceId);
    }


    @GetMapping("/{fragranceId}/review/preview")
    public GetFragranceReviewPreview getFragranceReviewPreview(@PathVariable("fragranceId") Long fragranceId) {
        return fragranceService.getFragranceReviewPreview(fragranceId);
    }


//    // TODO 리뷰 통계
//    @GetMapping("/{fragranceId}/review/statistics")
//    public GetFragranceReviewStatistics getFragranceReviewStatistics(@PathVariable("fragranceId") Long fragranceId) {
//        return fragranceService.getFragranceReviewStatistics(fragranceId);
//    }


    @GetMapping("/{fragranceId}/similar")
    public GetFragranceSimilar getFragranceSimilar(@PathVariable("fragranceId") Long fragranceId) {
        return fragranceService.getFragranceSimilar(fragranceId);
    }


    @GetMapping("/{fragranceId}/purchase")
    public GetFragrancePurchase getFragrancePurchase(@PathVariable("fragranceId") Long fragranceId) {
        return fragranceService.getFragrancePurchase(fragranceId);
    }


    @GetMapping("/{fragranceId}/review")
    public GetFragranceReview getFragranceReview(@PathVariable("fragranceId") Long fragranceId, PageRequest pageRequest) {
        return fragranceService.getFragranceReview(fragranceId, pageRequest);
    }


    @PostMapping("/{fragranceId}/scrap")
    public ResponseEntity<?> scrap(
            @PathVariable("fragranceId") Long fragranceId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return fragranceService.scrap(fragranceId, userDetails);
    }
}
