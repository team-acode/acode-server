package server.acode.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.acode.domain.family.dto.response.PageableResponse;
import server.acode.domain.user.dto.response.UserBasicInfoDto;
import server.acode.domain.user.service.UserService;
import server.acode.domain.user.dto.request.NicknameRequest;
import server.acode.global.common.PageRequest;
import server.acode.global.util.SecurityUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "My Page", description = "마이페이지 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "닉네임 설정",
            description = "온보딩 시 닉네임 설정/ 마이페이지에서 닉네임 수정")
    @PutMapping("/users/nickname")
    public void updateNickname(@RequestBody @Valid NicknameRequest request){
        Long userId = SecurityUtil.getCurrentUserId();
        userService.synchronizedUpdateNickname(request.getNickname(), userId);
    }


    @Operation(summary = "닉네임 중복 확인",
            description = "중복되는 닉네임이 있다면 409 에러 반환됩니다.")
    @PostMapping("/users/nickname")
    public void checkNickname(@RequestBody @Valid NicknameRequest request){
        userService.checkNickname(request.getNickname());
    }


    @Operation(summary = "마이페이지 기본 정보")
    @GetMapping("/mypage")
    public UserBasicInfoDto getUserBasicInfo(){
        Long userId = SecurityUtil.getCurrentUserId();
        return userService.getUserBasicInfo(userId);
    }

    @Operation(summary = "마이페이지 스크랩 리스트업",
            description = "페이지는 파라미터 없을 시 기본 page = 1, size = 10입니다.")
    @GetMapping("/mypage/scrap")
    public PageableResponse getUserScrap(PageRequest request){
        Long userId = SecurityUtil.getCurrentUserId();
        return userService.getScrapList(userId, request);
    }

    @Operation(summary = "마이페이지 리뷰 리스트업",
            description = "페이지는 파라미터 없을 시 기본 page = 1, size = 10입니다.")
    @GetMapping("/mypage/review")
    public PageableResponse getUserReview(PageRequest request){
        Long userId = SecurityUtil.getCurrentUserId();
        return userService.getReviewList(userId, request);
    }


}
