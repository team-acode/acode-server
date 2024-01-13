package server.acode.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import server.acode.domain.review.dto.request.RegisterReviewRequest;
import server.acode.domain.review.service.ReviewService;
import server.acode.domain.review.service.S3Service;
import server.acode.domain.user.entity.User;
import server.acode.domain.user.repository.UserRepository;
import server.acode.global.auth.security.CustomUserDetails;
import server.acode.global.common.ErrorCode;
import server.acode.global.exception.CustomException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final S3Service s3Service;
    private final ReviewService reviewService;

    private final UserRepository userRepository;

    @GetMapping("/image/{filename}")
    public ResponseEntity<?> registerImage(@PathVariable("filename") String filename) {
        return ResponseEntity.ok(s3Service.getPresignedUrl(filename));
    }

    @PostMapping("/{fragranceId}")
    public ResponseEntity<?> registerReview(
            @PathVariable("fragranceId") Long fragranceId,
            @RequestParam("userId") Long userId,
            @RequestBody RegisterReviewRequest registerReviewRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return reviewService.registerReview(fragranceId, registerReviewRequest, user);
    }

    @GetMapping("/admin")
    public ResponseEntity<?> insertStatistics() {
        return reviewService.insertStatistics();
    }
}
