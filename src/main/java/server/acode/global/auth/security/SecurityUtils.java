package server.acode.global.auth.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import server.acode.global.common.ErrorCode;
import server.acode.global.exception.CustomException;
public class SecurityUtils {

    public static String getCurrentUserAuthKey() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null || authentication.getName() == "anonymousUser") {
            throw new CustomException(ErrorCode.TOKEN_VALIDATION_EXCEPTION);
        }

        return authentication.getName();
    }
}
