package server.acode.global.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import server.acode.domain.user.entity.User;
import server.acode.domain.user.repository.UserRepository;
import server.acode.global.common.ErrorCode;
import server.acode.global.exception.CustomException;
@RequiredArgsConstructor
@Component
public class SecurityUtils {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null || authentication.getName() == "anonymousUser") {
            throw new CustomException(ErrorCode.TOKEN_VALIDATION_EXCEPTION);
        }

        User user = userRepository.findByAuthKeyAndIsDel(authentication.getName(), false)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return user;
    }
}
