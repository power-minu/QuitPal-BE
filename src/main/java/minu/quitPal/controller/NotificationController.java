package minu.quitPal.controller;

import lombok.RequiredArgsConstructor;
import minu.quitPal.entity.user.User;
import minu.quitPal.repository.UserRepository;
import minu.quitPal.service.NotificationService;
import minu.quitPal.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @PostMapping
    public void sendTestNotification() {
        User user = userRepository.findById(2L).orElseThrow(() -> new RuntimeException("회원 없음"));

        notificationService.sendNotification(user, "테스트를 위한 알림");
    }

}
