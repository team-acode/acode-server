package server.acode.global.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import server.acode.domain.user.entity.User;
import server.acode.domain.user.repository.UserRepository;
import server.acode.global.auth.dto.request.AccessTokenRequest;
import server.acode.global.auth.dto.response.TokenResponse;
import server.acode.global.auth.security.CustomUserDetails;
import server.acode.global.auth.security.SecurityUtils;
import server.acode.global.common.ErrorCode;
import server.acode.global.exception.CustomException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Auth", description = "인증 API")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;



    @GetMapping("/oauth2/kakao")
    @Operation(summary = "카카오 로그인", description = "카카오 로그인 후 발급받은 인증 코드를 넣어주세요")
    public ResponseEntity signin(@RequestParam("code") String code) throws JsonProcessingException {
        return authService.signin(code);
    }


    @GetMapping("/test/user/securityUtil")
    @Operation(summary = "유저 확인 테스트용", description = "개발자용입니다 토큰 넣고 호출 시 사용자 이름이 리턴됩니다")
    public void test(){
        authService.checkUser(SecurityUtils.getCurrentUserAuthKey());
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public void logout(HttpServletRequest request){
        authService.logout(request.getHeader("Authorization"));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/withdrawal")
    public ResponseEntity withdrawal(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails user){
        return authService.withdrawal(request.getHeader("Authorization"), user.getUsername());
    }

    @Operation(summary = "access token 재발급")
    @PostMapping("/reissue")
    public TokenResponse reissue(@AuthenticationPrincipal CustomUserDetails user, @RequestBody @Valid AccessTokenRequest request){
        return authService.reissueAccessToken(request.getRefreshToken(), user.getUsername());
    }


}
