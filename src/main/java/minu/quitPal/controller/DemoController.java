package minu.quitPal.controller;

import lombok.RequiredArgsConstructor;
import minu.quitPal.scheduler.NotificationScheduler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {

    private final NotificationScheduler notificationScheduler;

    @PostMapping("/noti")
    public ResponseEntity<String> demonstrateNotification() {
        notificationScheduler.sendDailyNotifications();
        return ResponseEntity.ok("알림 전송 완료");
    }

}