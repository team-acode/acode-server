package server.acode.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import server.acode.domain.review.dto.request.RegisterReviewRequest;
import server.acode.domain.review.service.ReviewService;
import server.acode.domain.review.service.S3Service;
import server.acode.global.util.SecurityUtil;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰 등록 API")
public class ReviewController {
    private final S3Service s3Service;
    private final ReviewService reviewService;


    @Operation(summary = "presignedUrl 발급",
            description = "client에서 발급받은 url에 PUT으로 이미지 저장")
    @GetMapping("/image/{filename}")
    public ResponseEntity<?> registerImage(@PathVariable("filename") String filename) {
        return ResponseEntity.ok(s3Service.getPresignedUrl(filename));
    }

    @Operation(summary = "리뷰 등록",
            description = "rate 별점: 1 이상 5 이하 정수로 할까욥 \n\n" +
                    "comment 한 줄 리뷰\n\n" +
                    "season 계절감: `SPRING`, `SUMMER`, `AUTUMN`, `WINTER`, `ALLSEASONS` \n\n" +
                    "longevity 지속성: `ONEHOUR`, `FOURHOURS`, `HALFDAY`, `FULLDAY` \n\n" +
                    "intensity 세기: `WEAK`, `MEDIUM`, `STRONG`, `INTENSE` \n\n" +
                    "style 스타일: 콤마랑 공백 `\", \"` 으로 이어 보내주세요 \n\n" +
                    "\t\tCHIC 시크한 | MATURE 성숙한 | LUXURIOUS 고급스러운 | ELEGANT 우아한 | MASCULINE 남성적인\n\n" +
                    "\t\tCOMFORTABLE 편안한 | SERENE 차분한 | LIGHT 가벼운 | NEUTRAL 중성적인 | FRIENDLY 친근한\n\n" +
                    "\t\tCLEAN 깨끗한 | SENSUAL 관능적인 | DELICATE 은은한 | LIVELY 활기찬 | LOVELY 사랑스러운\n\n" +
                    "\t\tBRIGHT 밝은 | RADIANT 화사한 | FEMININE 여성스러운 | INNOCENT 청순한 | WEIGHTY 무게감 있는\n\n" +
                    "\t\tSOFT 부드러운 | COZY 포근한")
    @PostMapping("/{fragranceId}")
    public ResponseEntity<?> registerReview(
            @PathVariable("fragranceId") Long fragranceId,
            @RequestBody RegisterReviewRequest registerReviewRequest) {
        Long userId = SecurityUtil.getCurrentUserId();
        return reviewService.registerReview(fragranceId, registerReviewRequest, userId);
    }

    @Operation(summary = "리뷰 삭제")
    @DeleteMapping("/review/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId){
        Long userId= SecurityUtil.getCurrentUserId();
        reviewService.deleteReview(reviewId, userId);
    }

    @Operation(summary = "관리자용입니다")
    @GetMapping("/admin")
    public ResponseEntity<?> insertStatistics() {
        return reviewService.insertStatistics();
    }
}
