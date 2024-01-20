package server.acode.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NicknameRequest {
    @NotNull(message = "닉네임이 입력되지 않았습니다.")
    private String nickname;
}
