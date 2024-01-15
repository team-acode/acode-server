package server.acode.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import server.acode.domain.review.dto.request.RegisterReviewRequest;
import server.acode.domain.review.service.ReviewService;
import server.acode.domain.review.service.S3Service;
import server.acode.global.auth.security.CustomUserDetails;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final S3Service s3Service;
    private final ReviewService reviewService;

    @GetMapping("/image/{filename}")
    public ResponseEntity<?> registerImage(@PathVariable("filename") String filename) {
        return ResponseEntity.ok(s3Service.getPresignedUrl(filename));
    }

    @PostMapping("/{fragranceId}")
    public ResponseEntity<?> registerReview(
            @PathVariable("fragranceId") Long fragranceId,
            @RequestBody RegisterReviewRequest registerReviewRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return reviewService.registerReview(fragranceId, registerReviewRequest, userDetails);
    }

    @GetMapping("/admin")
    public ResponseEntity<?> insertStatistics() {
        return reviewService.insertStatistics();
    }
}
