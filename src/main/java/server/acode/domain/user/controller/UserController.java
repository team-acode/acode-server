package server.acode.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.acode.domain.user.service.UserService;
import server.acode.global.auth.security.CustomUserDetails;

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
}
