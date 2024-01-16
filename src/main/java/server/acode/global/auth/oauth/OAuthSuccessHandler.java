package server.acode.global.auth.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import server.acode.domain.user.entity.User;
import server.acode.domain.user.repository.UserRepository;
import server.acode.global.auth.jwt.JwtTokenProvider;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

        String accessToken = jwtTokenProvider.createAccessToken(oAuth2User.getName(), "USER_ROLE"); // 토큰 생성
        System.out.println(accessToken);

        Optional<User> byAuthKey = userRepository.findByAuthKey(oAuth2User.getName());
        Boolean init = byAuthKey.get().getNickname().equals("init");


        String targetUrl = UriComponentsBuilder.fromUriString(setRedirectUrl(request.getServerName()))
                .queryParam("jwtAccessToken", accessToken)
                .queryParam("init", init.toString())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl); // access token과 함께 리다이렉트

    }

    /**
     * Redirect url set
     */
    private String setRedirectUrl(String url) {
        String redirect_url = null;
        if (url.equals("localhost")) {
            redirect_url = "http://localhost:8080/api/oauth/kakao/success";
        }
        if (url.equals("abcode.shop")) {
            redirect_url = "http://localhost:3000/login/kakao";
        }

        return redirect_url;

    }
}
