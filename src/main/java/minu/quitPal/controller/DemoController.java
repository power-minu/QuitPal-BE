package minu.quitPal.controller;

import lombok.RequiredArgsConstructor;
import minu.quitPal.scheduler.NotificationScheduler;
import minu.quitPal.scheduler.TransactionScheduler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {

    private final NotificationScheduler notificationScheduler;
    private final TransactionScheduler transactionScheduler;

    @PostMapping("/noti")
    public ResponseEntity<String> demonstrateNotification() {
        notificationScheduler.sendDailyNotifications();
        return ResponseEntity.ok("알림 전송 완료");
    }

    @PostMapping("/trans")
    public ResponseEntity<String> scheduledTransactionCreate() throws IOException, InterruptedException {
        transactionScheduler.getAccountTransactionHistories();
        return ResponseEntity.ok("거래내역 삽입 완료");
    }

}
