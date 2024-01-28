package server.acode.global.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.acode.domain.user.repository.UserRepository;
import server.acode.global.auth.dto.request.AccessTokenRequest;
import server.acode.global.auth.dto.response.TokenResponse;
import server.acode.global.auth.security.CustomUserDetails;
import server.acode.global.util.SecurityUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Auth", description = "인증 API")
public class AuthController {

    private final AuthService authService;


    @GetMapping("/oauth2/kakao")
    @Operation(summary = "배포용 카카오 로그인", description = "배포용 카카오 로그인 후 발급받은 인증 코드를 넣어주세요")
    public ResponseEntity signin(@RequestParam("code") String code) throws JsonProcessingException {
        return authService.signin(code, false);
    }

    @GetMapping("/oauth2/kakao/developer")
    @Operation(summary = "로컬용 카카오 로그인", description = "로컬용 카카오 로그인 후 발급받은 인증 코드를 넣어주세요")
    public ResponseEntity signinDeveloper(@RequestParam("code") String code) throws JsonProcessingException {
        return authService.signin(code, true);
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public void logout(HttpServletRequest request){
        authService.logout(request.getHeader("Authorization"));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/withdrawal")
    public ResponseEntity withdrawal(HttpServletRequest request){
        authService.logout(request.getHeader("Authorization"));  // 토큰 만료 처리
        return authService.withdrawal(SecurityUtil.getCurrentUserId());
    }

    @Operation(summary = "access token 재발급")
    @PostMapping("/reissue")
    public TokenResponse reissue(HttpServletRequest request, @RequestBody @Valid AccessTokenRequest dto){
        Long userId = SecurityUtil.getCurrentUserId();
        authService.logout(request.getHeader("Authorization"));  // 토큰 만료 처리
        return authService.reissueAccessToken(dto.getRefreshToken(), userId);
    }


}
