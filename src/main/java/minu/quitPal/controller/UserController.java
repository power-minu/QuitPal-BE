package minu.quitPal.controller;

import lombok.RequiredArgsConstructor;
import minu.quitPal.dto.PushTokenRequest;
import minu.quitPal.dto.UserResponseDto;
import minu.quitPal.entity.user.User;
import minu.quitPal.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyUserInfo() {
        UserResponseDto myInfoBySecurity = userService.getMyInfoBySecurity();
        System.out.println("myInfoBySecurity.getEmail() = " + myInfoBySecurity.getEmail());
        return ResponseEntity.ok(myInfoBySecurity);
    }

    @PostMapping("/push-token")
    public ResponseEntity<?> updatePushToken(@RequestBody PushTokenRequest request) {
        User user = userService.getCurrentUser();
        userService.updatePushToken(user, request.getPushToken());
        String response = user.getId().toString() + " " + request.getPushToken();
        System.out.println("푸시 토큰이 업데이트되었습니다. " + response);
        return ResponseEntity.ok("푸시 토큰이 업데이트되었습니다. " + response);
    }

}