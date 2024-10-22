package minu.quitPal.scheduler;

import lombok.RequiredArgsConstructor;
import minu.quitPal.entity.Transaction;
import minu.quitPal.entity.user.User;
import minu.quitPal.repository.TransactionRepository;
import minu.quitPal.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 9 * * ?")
    public void sendDailyNotifications() {
        // checked가 false인 모든 Transaction을 가져옵니다.
        List<Transaction> uncheckedTransactions = transactionRepository.findByCheckedAndExpired(false, false);

        // 각 Transaction의 User에게 알림을 보냅니다.
        Set<User> userSet = new HashSet<>();
        for (Transaction t : uncheckedTransactions) userSet.add(t.getUser());
        for (User u : userSet) notificationService.sendNotification(u, "당신이 검증해야 하는 결제 내역이 있습니다.");
    }

}
