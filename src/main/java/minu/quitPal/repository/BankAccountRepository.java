package minu.quitPal.repository;

import minu.quitPal.entity.user.BankAccount;
import minu.quitPal.entity.user.CodefConnectedId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findByCodefConnectedId(CodefConnectedId codefConnectedId);

}
