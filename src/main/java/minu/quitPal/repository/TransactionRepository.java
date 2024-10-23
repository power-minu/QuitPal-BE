package minu.quitPal.repository;

import minu.quitPal.entity.Transaction;
import minu.quitPal.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /*
    TODO: 생성되는 Transaction들은 codef에서 스케줄러로 들어올 거임. 얘네들은 전부 가입일 이후의 거래내역들.
    - 모든 커넥티드아이디들으로 조회해가지고 Transaction에 유저도 넣고 기록해줌.
    - 해당 회원에 대해 거래일자, 거래시각, 거래점이 모두 같은 내역이 있다면 저장하지 않을 거임.
     */

    List<Transaction> findByCheckedAndExpired(boolean checked, boolean expired);

    List<Transaction> findByUser(User user);

}
