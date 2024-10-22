package minu.quitPal.service;

import lombok.RequiredArgsConstructor;
import minu.quitPal.config.SecurityUtil;
import minu.quitPal.dto.UserResponseDto;
import minu.quitPal.entity.user.User;
import minu.quitPal.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto getMyInfoBySecurity() {
        return userRepository.findById(SecurityUtil.getCurrentUserId())
                .map(UserResponseDto::of)
                .orElseThrow(() -> new RuntimeException("유저 로그인 정보가 없습니다."));
    }

    public User getCurrentUser() {
        return userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("유저 로그인 정보가 없습니다."));
    }

    @Transactional
    public void updatePushToken(User user, String pushToken) {
        System.out.println(user.getId());
        user.setExpoPushToken(pushToken);
        userRepository.save(user);
    }

}
