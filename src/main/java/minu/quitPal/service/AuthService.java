package minu.quitPal.service;

import lombok.RequiredArgsConstructor;
import minu.quitPal.dto.TokenDto;
import minu.quitPal.dto.UserRequestDto;
import minu.quitPal.dto.UserResponseDto;
import minu.quitPal.entity.user.User;
import minu.quitPal.jwt.TokenProvider;
import minu.quitPal.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManagerBuilder managerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public UserResponseDto signUp(UserRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }

        User user = requestDto.toUser(passwordEncoder);
        return UserResponseDto.of(userRepository.save(user));
    }

    public TokenDto login(UserRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthenticationToken();

        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.generateTokenDto(authentication);
    }

}
