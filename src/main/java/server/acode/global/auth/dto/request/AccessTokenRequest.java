package server.acode.global.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccessTokenRequest {
    @NotNull(message = "리프레시 토큰이 입력되지 않았습니다.")
    private String refreshToken;
}
