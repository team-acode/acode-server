package server.acode.global.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
public class AuthService {

    private final RedisDao redisDao;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Value("${kakaoAdminKey}")
    private String adminKey;

    @Value("${KAKAO_CLIENT_SECRET}")
    private String kakaoClientSecret;

    public void checkUser(Long userId){
        /**
         * 사용자 정보 이용하는 부분 모두
         * String userId = SecurityUtils.getCurrentUserId();
         *
         * User user = useruserRepository.findById(Long.parseLong(userId))
         *       .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
         * 으로 수정해야함
         */
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        System.out.println("user = " + user.getNickname());
    }

    public ResponseEntity signin(String code) throws JsonProcessingException {
        String kakaoAccessToken = getKakaoAccessToken(code);
        String userInfo = getUserInfo(kakaoAccessToken);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(userInfo);

        // id 값을 추출
        String authKey = jsonNode.get("id").toString();

        HttpStatus init = HttpStatus.OK;
        if(!userRepository.existsByAuthKeyAndIsDel(authKey, false)) {
            createUser(authKey);  // 회원가입
            init = HttpStatus.CREATED;
        }

        TokenResponse token = TokenResponse.builder()
                .accessToken(jwtTokenProvider.createAccessToken(authKey, "USER_ROLE"))
                .refreshToken(jwtTokenProvider.createRefreshToken(authKey, "USER_ROLE"))
                .build();

        return new ResponseEntity<>(token, init);
    }

    // TODO Redirect url 숨기기
    private String getKakaoAccessToken(String code) throws JsonProcessingException {
        // header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // body 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); //고정값
        params.add("client_id", "3aaae9b053b685f95900145c213f769f");
        params.add("redirect_uri", "http://localhost:3000/login/oauth2/code/kakao"); //등록한 redirect uri
        params.add("code", code);
        params.add("client_secret", kakaoClientSecret);
        System.out.println(code);

        // header + body
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        // http 요청하기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());

        // access_token의 값을 읽어오기
        String accessToken = jsonNode.get("access_token").asText();

        System.out.println("Access Token: " + accessToken);
        return accessToken;

    }

    private String getUserInfo(String accessToken) {
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

    private User createUser(String authKey){
        User user = User.builder()
                .authKey(authKey)
                .build();

        return userRepository.save(user);
    }

    public void logout(String token) {
        redisDao.setValues(token.substring(7), "logout", jwtTokenProvider.getExpiration(token.substring(7)));
    }

    public ResponseEntity withdrawal(String token, Long userId) {
        logout(token); // 토큰 만료 처리

        // 내부 회원 탈퇴 처리
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


        // soft-delete
        currentUser.updateIsDel(true);
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

    public TokenResponse reissueAccessToken(String refreshToken, String authKey) {
        // 유효성 확인
        if(!jwtTokenProvider.validateToken(refreshToken)){
            throw new CustomException(ErrorCode.TOKEN_VALIDATION_EXCEPTION);
        }

        // authKey와 일치여부 확인
        String findAuthKey = redisDao.getValues(refreshToken);
        if(!findAuthKey.equals(authKey)){
            throw new CustomException(ErrorCode.TOKEN_VALIDATION_EXCEPTION);
        }

        return TokenResponse.builder()
                .accessToken(jwtTokenProvider.createAccessToken(authKey, "USER_ROLE"))
                .refreshToken(jwtTokenProvider.createRefreshToken(authKey, "USER_ROLE"))
                .build();
    }


}
