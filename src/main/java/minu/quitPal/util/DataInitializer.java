package minu.quitPal.util;

import lombok.RequiredArgsConstructor;
import minu.quitPal.codef.api.EasyCodefProperties;
import minu.quitPal.entity.Transaction;
import minu.quitPal.entity.user.Authority;
import minu.quitPal.entity.user.User;
import minu.quitPal.repository.TransactionRepository;
import minu.quitPal.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // User 더미 데이터 생성
        User user1 = User.builder()
                .email("admin@example.com")
                .password(passwordEncoder.encode("admin"))
                .authority(Authority.ROLE_ADMIN)
                .birthDate("700101")
                .build();

        User mw = User.builder()
                .email("mw0677@naver.com")
                .password(passwordEncoder.encode("alsdn1"))
                .authority(Authority.ROLE_USER)
                .birthDate("000706")
                .build();

        userRepository.save(user1);
        userRepository.save(mw);

        mw.setJoinDate(LocalDate.of(2024, 10, 15));

        // Transaction 더미 데이터 생성
        Transaction transaction1 = Transaction.builder()
                .place("목감씨유")
                .amount(6000)
                .purchaseDate(LocalDate.of(2024, 10, 20))
                .build();
        transaction1.assignUser(mw);
        transactionRepository.save(transaction1);

        Transaction transaction2 = Transaction.builder()
                .place("마포GS")
                .amount(5000)
                .purchaseDate(LocalDate.of(2024, 10, 21))
                .build();
        transaction2.assignUser(mw);
        transactionRepository.save(transaction2);

        Transaction transaction3 = Transaction.builder()
                .place("세븐일레븐문정수정")
                .amount(13820)
                .purchaseDate(LocalDate.of(2020, 6, 9))
                .build();
        transaction3.assignUser(mw);
        transactionRepository.save(transaction3);

        System.out.println("Dummy data initialization completed.");
    }

}
