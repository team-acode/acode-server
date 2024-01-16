package server.acode.global.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import server.acode.domain.user.entity.User;
import server.acode.domain.user.repository.UserRepository;
import server.acode.global.auth.dto.response.JwtTokenResponse;
import server.acode.global.auth.jwt.JwtTokenProvider;
import server.acode.global.common.ErrorCode;
import server.acode.global.exception.CustomException;
import server.acode.global.inmemory.RedisDao;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final RedisDao redisDao;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Value("${kakaoAdminKey}")
    private String adminKey;

    public void logout(String token, String authKey) {
        redisDao.setValues(token.substring(7), "logout", jwtTokenProvider.getExpiration(token.substring(7)));
    }

    public ResponseEntity withdrawal(String token, String authKey) {
        logout(token, authKey); // 토큰 만료 처리

        // 내부 회원 탈퇴 처리
        // TODO 탈퇴한 회원 재가입 고민
        User byAuthKey = userRepository.findByAuthKey(authKey)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        byAuthKey.updateIsDel(true);
        userRepository.save(byAuthKey);

        // 카카오 unlink 처리
        // 요청 URL
        String url = "https://kapi.kakao.com/v1/user/unlink";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + adminKey);

        // 요청 바디 설정
        String requestBody = "target_id_type=user_id&target_id=" + authKey;

        // HTTP 요청 엔터티 생성
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // RestTemplate 생성
        RestTemplate restTemplate = new RestTemplate();

        // HTTP POST 요청
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // 응답 출력
        System.out.println("Response Code: " + responseEntity.getStatusCode());
        System.out.println("Response Body: " + responseEntity.getBody());

        return new ResponseEntity(HttpStatus.OK, responseEntity.getStatusCode());

    }

    public JwtTokenResponse reissueAccessToken(String refreshToken, String authKey) {
        // 유효성 확인
        if(!jwtTokenProvider.validateToken(refreshToken)){
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        // authKey와 일치여부 확인
        String findAuthKey = redisDao.getValues(refreshToken);
        if(!findAuthKey.equals(authKey)){
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        return JwtTokenResponse.builder()
                .accessToken(jwtTokenProvider.createAccessToken(authKey, "USER_ROLE"))
                .refreshToken(jwtTokenProvider.createRefreshToken(authKey, "USER_ROLE"))
                .build();
    }
}
