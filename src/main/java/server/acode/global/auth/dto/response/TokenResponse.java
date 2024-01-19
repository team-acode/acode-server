package server.acode.global.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private Boolean init;

    @Builder
    public TokenResponse(String accessToken, String refreshToken, Boolean init){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.init = init;
    }
}
