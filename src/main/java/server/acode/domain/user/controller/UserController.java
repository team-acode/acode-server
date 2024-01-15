package server.acode.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import server.acode.domain.family.dto.response.PageableResponse;
import server.acode.domain.user.dto.response.PreviewUserInfo;
import server.acode.domain.user.service.UserService;
import server.acode.global.auth.security.CustomUserDetails;
import server.acode.global.common.PageRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "My Page", description = "마이페이지 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "닉네임 설정",
            description = "온보딩 시 닉네임 설정/ 마이페이지에서 닉네임 수정")
    @PutMapping("/nickname/{nickname}")
    public void updateNickname(@PathVariable("nickname") String nickname){
        UserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.updateNickname(nickname, user.getUsername());
    }

    @Operation(summary = "마이페이지 기본 정보")
    @GetMapping("/mypage")
    public PreviewUserInfo getUserInfo(@AuthenticationPrincipal CustomUserDetails user){
        return userService.getUserInfo(user.getUsername());
    }

    @Operation(summary = "마이페이지 스크랩 리스트업",
            description = "페이지는 파라미터 없을 시 기본 page = 1, size = 10입니다.")
    @GetMapping("/mypage/scrap")
    public PageableResponse getUserScrap(PageRequest request, @AuthenticationPrincipal CustomUserDetails user){
        return userService.getScrapList(user.getUsername(), request);
    }

    @Operation(summary = "마이페이지 리뷰 리스트업",
            description = "페이지는 파라미터 없을 시 기본 page = 1, size = 10입니다.")
    @GetMapping("/mypage/review")
    public PageableResponse getUserReview(PageRequest request, @AuthenticationPrincipal CustomUserDetails user){
        return userService.getReviewList(user.getUsername(), request);
    }


}
