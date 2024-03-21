package server.acode.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.family.dto.response.PageableResponse;
import server.acode.domain.review.repository.ReviewRepository;
import server.acode.domain.user.dto.response.ReviewDto;
import server.acode.domain.user.dto.response.ScrapDto;
import server.acode.domain.user.dto.response.ScrapPreviewDto;
import server.acode.domain.user.dto.response.UserBasicInfoDto;
import server.acode.domain.user.entity.User;
import server.acode.domain.user.repository.ScrapRepository;
import server.acode.domain.user.repository.UserRepository;
import server.acode.global.common.ErrorCode;
import server.acode.global.exception.CustomException;
import server.acode.global.common.PageRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ScrapRepository scrapRepository;

    public synchronized void synchronizedUpdateNickname(String nickname, Long userId){
        updateNickname(nickname, userId);
    }

    @Transactional
    public void updateNickname(String nickname, Long userId) {
        checkDuplicatedNickname(nickname);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.setNickname(nickname);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public void checkDuplicatedNickname(String nickname) {
        userRepository.findByNicknameAndIsDel(nickname, false).ifPresent(user -> {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_USED);
        });
    }

    @Transactional(readOnly = true)
    public UserBasicInfoDto getUserBasicInfo(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<ScrapPreviewDto> scrapPreview = scrapRepository.getScrapPreview(userId);

        UserBasicInfoDto response = UserBasicInfoDto.builder()
                .nickname(user.getNickname())
                .reviewCnt(reviewRepository.countByUserId(userId))
                .scraps(scrapPreview)
                .build();

        return response;
    }

    @Transactional(readOnly = true)
    public PageableResponse getScrapList(Long userId, PageRequest pageRequest){
        Pageable pageable = pageRequest.of(); // pageable 객체로 변환

        Page<ScrapDto> result = scrapRepository.getScrap(userId, pageable);
        return new PageableResponse(result.getContent(), result.getTotalPages(), result.getTotalElements());
    }

    @Transactional(readOnly = true)
    public PageableResponse getReviewList(Long userId, PageRequest pageRequest) {
        Pageable pageable = pageRequest.of(); // pageable 객체로 변환

        Page<ReviewDto> result = reviewRepository.getDisplayReview(userId, pageable);
        return new PageableResponse(result.getContent(), result.getTotalPages(), result.getTotalElements());
    }

}