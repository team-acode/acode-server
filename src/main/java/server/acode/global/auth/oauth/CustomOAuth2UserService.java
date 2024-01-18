package server.acode.global.auth.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.user.entity.User;
import server.acode.domain.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        /**
         * 서드파티 접근을 위한 accessToken까지 얻은다음 실행됨
         */

        OAuth2User oAuth2User = super.loadUser(userRequest);  // accessToken으로 서드파티에 요청해서 사용자 정보를 얻어옴
        String registrationId = userRequest.getClientRegistration().getRegistrationId();  // provider(kakao) 가져옴

        String authKey = oAuth2User.getName();  // 고유식별자 가져오기

        Map<String, Object> properties = (Map<String, Object>) oAuth2User.getAttributes().get("properties");
        String nickname = (String) properties.get("nickname"); // 닉네임 가져오기

        User user;
        if(!userRepository.existsByAuthKey(authKey)) {
            // 회원가입
            user = createUser(authKey, "init"); // 닉네임으로 회원 가입 구분

        } else {
            System.out.println(oAuth2User.getName());
            user = userRepository.findByAuthKey(oAuth2User.getName()).orElseThrow();


        }

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString()))
                , oAuth2User.getAttributes(), "id");
    }


    public User createUser(String authKey, String nickname){
        User user = User.builder()
                .authKey(authKey)
                .nickname(nickname)
                .build();

        return userRepository.save(user);
    }

}
