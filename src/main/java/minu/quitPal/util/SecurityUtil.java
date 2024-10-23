package minu.quitPal.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecurityUtil {

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security context에 인증 정보가 없습니다.");
        }

        return Long.parseLong(authentication.getName());
    }

}
