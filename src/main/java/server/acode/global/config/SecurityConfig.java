package server.acode.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import server.acode.global.auth.jwt.JwtAuthenticationFilter;
import server.acode.global.auth.oauth.CustomOAuth2UserService;
import server.acode.global.auth.oauth.OAuthSuccessHandler;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuthSuccessHandler oauthSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().permitAll())
                .oauth2Login(oauth2Configurer -> oauth2Configurer
                        .successHandler(oauthSuccessHandler)
                        .userInfoEndpoint()
                        .userService(oAuth2UserService))

                .cors(cors -> cors.disable()) // cors 설정 추가 예정
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)


                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .formLogin(formLogin -> formLogin.disable()) // 사용자 지정 로그인 로직 구현
                .httpBasic(HttpBasicConfigurer::disable); // http 기본 인증 비활성화

        return http.build();
    }

}
