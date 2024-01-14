package server.acode.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.user.entity.User;
import server.acode.domain.user.repository.UserRepository;
import server.acode.global.auth.security.CustomUserDetails;
import server.acode.global.common.ErrorCode;
import server.acode.global.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void updateNickname(String nickname, String authKey) {

        User byAuthKey = userRepository.findByAuthKey(authKey)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        byAuthKey.updateNickname(nickname);
        userRepository.save(byAuthKey);
    }
}
