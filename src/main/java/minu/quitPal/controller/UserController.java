package minu.quitPal.controller;

import lombok.RequiredArgsConstructor;
import minu.quitPal.dto.UserResponseDto;
import minu.quitPal.entity.user.User;
import minu.quitPal.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}