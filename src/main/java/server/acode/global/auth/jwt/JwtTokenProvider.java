package server.acode.global.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.time.Duration;

import server.acode.global.auth.security.CustomUserDetailService;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements InitializingBean {
    private Key key;
    private final CustomUserDetailService customUserDetailsService;

    @Value("${token.secret}")
    private String secret;
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60; // 1시간
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 14; // 2주

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT AccessToken 생성
    public String createAccessToken(String authKey, String role) {
        Claims claims = Jwts.claims().setSubject(authKey); // JWT payload 에 저장되는 정보단위
        claims.put("role", role); // 정보는 key/value 쌍으로 저장
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuer("Acode")
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, this.key) // 비밀키로 서명
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(getUserAuthKey(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserAuthKey(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    // token의 유효성 + 만료일자 확인
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date()); // 만료시간 안지났다면 true
        } catch (Exception e) {
            return false;
        }
    }


    // token의 만료일자 가져오기
    public Duration getExpiration(String token) {
        Date expiration = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration();
        System.out.println("expiration = " + expiration);
        return Duration.between(Instant.now(), expiration.toInstant()); // 현재 시간과 토큰의 만료 시간 사이의 Duration 계산
    }

}
