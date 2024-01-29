package server.acode.domain.review.service;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.fragrance.repository.FragranceRepository;
import server.acode.domain.review.dto.request.RegisterReviewRequest;
import server.acode.domain.review.entity.*;
import server.acode.domain.review.repository.*;
import server.acode.domain.user.entity.User;
import server.acode.domain.user.repository.UserRepository;
import server.acode.global.common.ErrorCode;
import server.acode.global.exception.CustomException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final FragranceRepository fragranceRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    private final ReviewSeasonRepository reviewSeasonRepository;
    private final ReviewLongevityRepository reviewLongevityRepository;
    private final ReviewIntensityRepository reviewIntensityRepository;
    private final ReviewStyleRepository reviewStyleRepository;
    private final ReviewUpdateRepository reviewUpdateRepository;

    public ResponseEntity<?> registerReview(Long fragranceId, RegisterReviewRequest registerReviewRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //리뷰 엔티티 생성
        Fragrance fragrance = fragranceRepository.findById(fragranceId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRAGRANCE_NOT_FOUND));

//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        reviewRepository.save(registerReviewRequest.toEntity(user, fragrance));

        //사용자 리뷰 수 컬럼 증가
        userRepository.increaseReviewCnt(user.getId());
        System.out.println(user.getNickname());

        //향수 리뷰 수, 평점 총점 컬럼 증가
        fragranceRepository.increaseReview(registerReviewRequest.getRate(), fragranceId);

        //리뷰 관련 엔티티 설정 - 계절감 지속성 세기 스타일
        ReviewSeason reviewSeason = reviewSeasonRepository.findByFragrance(fragrance)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_SEASON_NOT_FOUND));
        String season = registerReviewRequest.getSeason().toString();
        if (season.equals("ALLSEASONS")) {
            season = "allSeasons";
        } else {
            season = season.toLowerCase();
        }
        increaseReviewSeason(reviewSeason, season);

        ReviewLongevity reviewLongevity = reviewLongevityRepository.findByFragrance(fragrance)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_LONGEVITY_NOT_FOUND));
        String longevity = registerReviewRequest.getLongevity().toString().toLowerCase();
        increaseReviewLongevity(reviewLongevity, longevity);

        ReviewIntensity reviewIntensity = reviewIntensityRepository.findByFragrance(fragrance)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_INTENSITY_NOT_FOUND));
        String intensity = registerReviewRequest.getIntensity().toString().toLowerCase();
        increaseReviewIntensity(reviewIntensity, intensity);

        ReviewStyle reviewStyle = reviewStyleRepository.findByFragrance(fragrance)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_STYLE_NOT_FOUND));
        String styleList = registerReviewRequest.getStyle().toLowerCase();
        List<String> keywordList = Arrays.asList(styleList.split(", "));
        keywordList.forEach(keyword -> increaseReviewStyle(reviewStyle, keyword));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private void increaseReviewSeason(ReviewSeason reviewSeason, String season) {
        try {
            // ReviewSeason 클래스에서 지정된 계절에 해당하는 필드를 가져오기 위해 리플렉션 사용
            // getDeclaredField 메소드 : 클래스의 모든 필드 중에서 지정된 이름의 필드 반환
            Field field = ReviewSeason.class.getDeclaredField(season);

            // 리플랙션 사용 시, 가져온 필드에 대한 접근 권한 설정
            // setAccessible(true)로 private 필드 접근 가능
            field.setAccessible(true);

            // 가져온 필드의 현재 값 읽어오기 근데 정수로
            int fieldValue = (int) field.get(reviewSeason);

            // 필드 값 1 증가
            field.set(reviewSeason, fieldValue + 1);

            field.setAccessible(false);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void increaseReviewLongevity(ReviewLongevity reviewLongevity, String longevity) {
        try {
            // 클래스에서 지정된 필드를 가져오기 위해 리플렉션 사용
            // getDeclaredField 메소드 : 클래스의 모든 필드 중에서 지정된 이름의 필드 반환
            Field field = ReviewLongevity.class.getDeclaredField(longevity);

            // 리플랙션 사용 시, 가져온 필드에 대한 접근 권한 설정
            // setAccessible(true)로 private 필드 접근 가능
            field.setAccessible(true);

            // 가져온 필드의 현재 값 읽어오기 근데 정수로
            int fieldValue = (int) field.get(reviewLongevity);

            // 필드 값 1 증가
            field.set(reviewLongevity, fieldValue + 1);

            field.setAccessible(false);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void increaseReviewIntensity(ReviewIntensity reviewIntensity, String intensity) {
        try {
            // 클래스에서 지정된 필드를 가져오기 위해 리플렉션 사용
            // getDeclaredField 메소드 : 클래스의 모든 필드 중에서 지정된 이름의 필드 반환
            Field field = ReviewIntensity.class.getDeclaredField(intensity);

            // 리플랙션 사용 시, 가져온 필드에 대한 접근 권한 설정
            // setAccessible(true)로 private 필드 접근 가능
            field.setAccessible(true);

            // 가져온 필드의 현재 값 읽어오기 근데 정수로
            int fieldValue = (int) field.get(reviewIntensity);

            // 필드 값 1 증가
            field.set(reviewIntensity, fieldValue + 1);

            field.setAccessible(false);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void increaseReviewStyle(ReviewStyle reviewStyle, String style) {
        try {
            // 클래스에서 지정된 필드를 가져오기 위해 리플렉션 사용
            // getDeclaredField 메소드 : 클래스의 모든 필드 중에서 지정된 이름의 필드 반환
            Field field = ReviewStyle.class.getDeclaredField(style);

            // 리플랙션 사용 시, 가져온 필드에 대한 접근 권한 설정
            // setAccessible(true)로 private 필드 접근 가능
            field.setAccessible(true);

            // 가져온 필드의 현재 값 읽어오기 근데 정수로
            int fieldValue = (int) field.get(reviewStyle);

            // 필드 값 1 증가
            field.set(reviewStyle, fieldValue + 1);

            field.setAccessible(false);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public ResponseEntity<?> insertStatistics() {
        for (int i = 1; i < 51; i++) {
            Long fragranceId = (long) i;
            reviewSeasonRepository.insertStatistics(fragranceId);
            reviewLongevityRepository.insertStatistics(fragranceId);
            reviewIntensityRepository.insertStatistics(fragranceId);
            reviewStyleRepository.insertStatistics(fragranceId);
        }
        return ResponseEntity.ok().build();
    }

    public void deleteReview(Long reviewId, Long userId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        if (review.getUser().getId() == userId) {
            /**
             *  ReviewSeason, ReviewLongevity, ReviewIntensity, ReviewStyle에 해당하는 컬럼 값 -= 1
             */
            String season = review.getSeason().toString().toLowerCase();
            reviewUpdateRepository.updateSeason(season, review.getFragrance().getId(), -1);

            String longevity = review.getLongevity().toString().toLowerCase(); // 지속성
            reviewUpdateRepository.updateLongevity(longevity, review.getFragrance().getId(), -1);

            String intensity = review.getIntensity().toString().toLowerCase(); // 향의 세기
            reviewUpdateRepository.updateIntensity(intensity, review.getFragrance().getId(), -1);

            String styleList = review.getStyle().toLowerCase();
            List<String> styles = Arrays.asList(styleList.split(", "));
            styles.forEach(style -> reviewUpdateRepository.updateStyle(style, review.getFragrance().getId(), -1));


            /**
             * Fragrance 테이블의 reviewCnt -= 1
             */
            fragranceRepository.decreaseReview(review.getRate(), review.getFragrance().getId());

            /**
             * Review 테이블 삭제
             */
            reviewRepository.delete(review);

        } else { throw new CustomException(ErrorCode.REVIEW_AUTHOR_MISMATCH); }
    }
}
