package server.acode.global.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import server.acode.domain.user.entity.User;
import server.acode.domain.user.repository.UserRepository;
import server.acode.global.auth.dto.response.JwtTokenResponse;
import server.acode.global.auth.security.CustomUserDetails;
import server.acode.global.common.ErrorCode;
import server.acode.global.exception.CustomException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Auth", description = "인증 API")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @GetMapping("/test/user")
    @Operation(summary = "유저 확인 테스트용", description = "개발자용입니다 토큰 넣고 호출 시 사용자 이름이 리턴됩니다")
    public String test(){
        UserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User byAuthKey = userRepository.findByAuthKey(user.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return byAuthKey.getNickname();
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails user){

        authService.logout(request.getHeader("Authorization"), user.getUsername());
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/withdrawal")
    public ResponseEntity withdrawal(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails user){
        return authService.withdrawal(request.getHeader("Authorization"), user.getUsername());
    }

    @Operation(summary = "access token 재발급")
    @PostMapping("/reissue")
    public JwtTokenResponse reissue(@AuthenticationPrincipal CustomUserDetails user, String refreshToken){
        // TODO 쿼리 파라미터말고 request body로 받기
        return authService.reissueAccessToken(refreshToken, user.getUsername());
    }



}
