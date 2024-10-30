package minu.quitPal.service;

import lombok.RequiredArgsConstructor;
import minu.quitPal.dto.TransactionResponse;
import minu.quitPal.entity.Transaction;
import minu.quitPal.entity.user.User;
import minu.quitPal.repository.TransactionRepository;
import minu.quitPal.repository.UserRepository;
import minu.quitPal.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public List<TransactionResponse> getMyTransactionList() {
        User curUser = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(() -> new RuntimeException("회원 찾기 실패"));
        List<Transaction> transactions = transactionRepository.findByUser(curUser);

        return transactions.stream().map(TransactionResponse::from).toList();
    }

    @Transactional
    public Long checkTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("거래 찾기 실패"));
        transaction.setChecked(true);
        return transaction.getId();
    }

}
