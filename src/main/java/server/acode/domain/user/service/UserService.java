package server.acode.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.family.dto.response.PageableResponse;
import server.acode.domain.review.repository.ReviewRepository;
import server.acode.domain.user.dto.response.DisplayReview;
import server.acode.domain.user.dto.response.DisplayScrap;
import server.acode.domain.user.dto.response.PreviewScrap;
import server.acode.domain.user.dto.response.PreviewUserInfo;
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
        checkNickname(nickname);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateNickname(nickname);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public void checkNickname(String nickname) {
        userRepository.findByNicknameAndIsDel(nickname, false).ifPresent(user -> {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_USED);
        });
    }

    @Transactional(readOnly = true)
    public PreviewUserInfo getUserInfo(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<PreviewScrap> previewScrap = scrapRepository.getScrapPreview(userId);

        PreviewUserInfo info = PreviewUserInfo.builder()
                .nickname(user.getNickname())
                .reviewCnt(reviewRepository.countByUserId(userId))
                .scraps(previewScrap)
                .build();

        return info;
    }

    @Transactional(readOnly = true)
    public PageableResponse getScrapList(Long userId, PageRequest pageRequest){
        Pageable pageable = pageRequest.of(); // pageable 객체로 변환

        Page<DisplayScrap> result = scrapRepository.getScrap(userId, pageable);
        return new PageableResponse(result.getContent(), result.getTotalPages(), result.getTotalElements());
    }

    @Transactional(readOnly = true)
    public PageableResponse getReviewList(Long userId, PageRequest pageRequest) {
        Pageable pageable = pageRequest.of(); // pageable 객체로 변환

        Page<DisplayReview> result = reviewRepository.getDisplayReview(userId, pageable);
        return new PageableResponse(result.getContent(), result.getTotalPages(), result.getTotalElements());
    }


}
