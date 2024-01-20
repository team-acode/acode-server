package server.acode.global.util;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import server.acode.global.common.ErrorCode;
import server.acode.global.exception.CustomException;
public class SecurityUtil {

    public static Long getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null || authentication.getName() == "anonymousUser") {
            throw new CustomException(ErrorCode.TOKEN_VALIDATION_EXCEPTION);
        }

        return Long.valueOf(authentication.getName());
    }
}
