package server.acode.global.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import server.acode.domain.user.entity.User;
import server.acode.domain.user.repository.UserRepository;
import server.acode.global.auth.dto.response.TokenResponse;
import server.acode.global.auth.jwt.JwtTokenProvider;
import server.acode.global.common.ErrorCode;
import server.acode.global.exception.CustomException;
import server.acode.global.inmemory.RedisDao;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final RedisDao redisDao;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Value("${kakaoAdminKey}")
    private String adminKey;

    @Value("${KAKAO_CLIENT_SECRET}")
    private String kakaoClientSecret;

    @Value("${KAKAO_REDIRECT_URL}")
    private String redirectedUrl;

    public ResponseEntity signin(String code, boolean developer) {
        String kakaoAccessToken = getKakaoAccessToken(code, developer);
        String userInfo = getKakaoInfo(kakaoAccessToken);
        String authKey = sendParseValue(userInfo, "id");

        HttpStatus init = HttpStatus.OK;
        if(!userRepository.existsByAuthKeyAndIsDel(authKey, false)) {
            createUser(authKey);// 회원가입
            init = HttpStatus.CREATED;
        }

        TokenResponse token = TokenResponse.builder()
                .accessToken(jwtTokenProvider.createAccessToken(authKey, "USER_ROLE"))
                .refreshToken(jwtTokenProvider.createRefreshToken(authKey, "USER_ROLE"))
                .build();

        return new ResponseEntity<>(token, init);
    }

    private String getKakaoAccessToken(String code, boolean developer){
        // header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // body 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); //고정값
        params.add("client_id", "3aaae9b053b685f95900145c213f769f");
        params.add("code", code);
        params.add("client_secret", kakaoClientSecret);
        if(developer){
            params.add("redirect_uri", "http://localhost:3000/login/kakao"); // 개발용 redirect uri
        } else {
            params.add("redirect_uri", redirectedUrl); // 배포용 redirect uri
        }

        // header + body
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        // http 요청하기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );
        } catch (Exception e){
            log.error("사용자의 카카오 로그인 인증 코드가 유효하지 않습니다.: " + e.toString());
            throw new CustomException(ErrorCode.INVALID_AUTHENTICATION_CODE);
        }

        String accessToken = sendParseValue(response.getBody(), "access_token");
        log.info("Access Token: " + accessToken);
        return accessToken;
    }

    private String getKakaoInfo(String accessToken) {
        // header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + accessToken);

        // http 요청하기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

       return response.getBody();
    }

    private String sendParseValue(String jsonInfo, String parsingKey){
        try {
            return tryToParseValue(jsonInfo, parsingKey);
        } catch (JsonProcessingException e) {
            log.error("카카오 응답 과정 중 json parsing error: " + e.toString());
            throw new CustomException(ErrorCode.JSON_PARSING_ERROR);
        }
    }

    private String tryToParseValue(String jsonInfo, String parsingKey) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        jsonNode = objectMapper.readTree(jsonInfo); // 예외 발생 지점

        // id 값을 추출
        String parsingValue = jsonNode.get(parsingKey).toString();
        return parsingValue;
    }

    private User createUser(String authKey){
        User user = User.builder()
                .authKey(authKey)
                .build();

        return userRepository.save(user);
    }

    public void logout(String token) {
        redisDao.setValues(token.substring(7), "logout", jwtTokenProvider.getExpiration(token.substring(7)));
    }

    public ResponseEntity withdrawal(Long userId) {
        // 내부 회원 탈퇴 처리
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


        // soft-delete
        currentUser.setIsDel(true);
        userRepository.save(currentUser);

        /** 카카오 unlink 처리 **/
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + adminKey);

        // 바디 설정
        String requestBody = "target_id_type=user_id&target_id=" + currentUser.getAuthKey();

        // HTTP 요청 엔터티 생성
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // RestTemplate 생성
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://kapi.kakao.com/v1/user/unlink",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return new ResponseEntity(HttpStatus.OK, responseEntity.getStatusCode());

    }

    public TokenResponse reissueAccessToken(String refreshToken, Long userId) {

        // 유효성 확인
        if(!jwtTokenProvider.validateToken(refreshToken)){
            throw new CustomException(ErrorCode.TOKEN_VALIDATION_EXCEPTION);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // authKey와 일치여부 확인
        String findAuthKey = redisDao.getValues(refreshToken);
        if(!findAuthKey.equals(user.getAuthKey())){
            throw new CustomException(ErrorCode.TOKEN_VALIDATION_EXCEPTION);
        }

        return TokenResponse.builder()
                .accessToken(jwtTokenProvider.createAccessToken(user.getAuthKey(), "USER_ROLE"))
                .build();
    }


}
