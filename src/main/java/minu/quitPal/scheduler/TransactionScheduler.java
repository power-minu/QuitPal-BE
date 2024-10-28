package minu.quitPal.scheduler;

import lombok.RequiredArgsConstructor;
import minu.quitPal.TransactionMapper;
import minu.quitPal.codef.api.EasyCodef;
import minu.quitPal.codef.api.EasyCodefServiceType;
import minu.quitPal.dto.UserResponseDto;
import minu.quitPal.entity.Transaction;
import minu.quitPal.entity.user.BankAccount;
import minu.quitPal.entity.user.CodefConnectedId;
import minu.quitPal.entity.user.User;
import minu.quitPal.repository.BankAccountRepository;
import minu.quitPal.repository.CodefConnectedIdRepository;
import minu.quitPal.repository.TransactionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionScheduler {

    private final CodefConnectedIdRepository codefConnectedIdRepository;
    private final BankAccountRepository bankAccountRepository;
    private final EasyCodef easyCodef;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    //    @Scheduled(cron = "0 0 9 * * ?")
    public void getAccountTransactionHistories() throws IOException, InterruptedException {
        List<CodefConnectedId> codefConnectedIds = codefConnectedIdRepository.findAll();

        for (CodefConnectedId c : codefConnectedIds) {
            HashMap<String, Object> params = new HashMap<>();
            User curUser = c.getUser();
            BankAccount bankAccount = bankAccountRepository.findByCodefConnectedId(c).orElseThrow(() -> new RuntimeException("계좌 못찾았다."));
            String account = bankAccount.getAccountNumber();
            String accountPassword = bankAccount.getAccountPassword();
            params.put("organization", "0004");
            params.put("startDate", "20241022");
            params.put("endDate", "20241028");
            params.put("orderBy", "0");
            params.put("inquiryType", "0");
            params.put("account", account);
            params.put("accountPassword", accountPassword);
            params.put("birthDate", curUser.getBirthDate());
            params.put("connectedId", c.getConnectedId());

            String result = easyCodef.requestProduct("/v1/kr/bank/p/account/transaction-list", EasyCodefServiceType.DEMO, params);

            List<Transaction> transactions = transactionMapper.mapTransactions(result);
            for (Transaction t : transactions) {
                t.assignUser(curUser);
                if (!transactionRepository.existsByPlaceAndAmountAndPurchaseDateAndPurchaseTimeAndUser(
                        t.getPlace(), t.getAmount(), t.getPurchaseDate(), t.getPurchaseTime(), t.getUser()
                )
                        // && storeRepository.jaccardExistsStoreName(t.getPlace()) place 보고 담배파는곳인지 아닌지 판단
                ) {
                    transactionRepository.save(t);
                }
            }
        }
    }

}
