package minu.quitPal.controller.codef;

import lombok.RequiredArgsConstructor;
import minu.quitPal.codef.api.EasyCodef;
import minu.quitPal.codef.api.EasyCodefProperties;
import minu.quitPal.codef.api.EasyCodefServiceType;
import minu.quitPal.codef.api.EasyCodefUtil;
import minu.quitPal.dto.UserResponseDto;
import minu.quitPal.entity.user.BankAccount;
import minu.quitPal.entity.user.CodefConnectedId;
import minu.quitPal.entity.user.User;
import minu.quitPal.repository.BankAccountRepository;
import minu.quitPal.repository.CodefConnectedIdRepository;
import minu.quitPal.repository.UserRepository;
import minu.quitPal.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CodefTransactionController {

    private final EasyCodef easyCodef;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CodefConnectedIdRepository codefConnectedIdRepository;
    private final BankAccountRepository bankAccountRepository;

    @GetMapping("/codef/trans/onetouch")
    public ResponseEntity<String> getAccountTransactionHistoryOneTouch() throws IOException, InterruptedException {
        HashMap<String, Object> params = new HashMap<>();
        UserResponseDto myInfoBySecurity = userService.getMyInfoBySecurity();
        User loggedInUser = userRepository.findUserByEmail(myInfoBySecurity.getEmail()).orElseThrow(() -> new RuntimeException("유저 못찾았어요."));
        CodefConnectedId connectedInfoByUser = codefConnectedIdRepository.findCodefConnectedIdByUser(loggedInUser).orElseThrow(() -> new RuntimeException("커넥티드아이디를 못찾았다."));
        BankAccount bankAccount = bankAccountRepository.findByCodefConnectedId(connectedInfoByUser).orElseThrow(() -> new RuntimeException("계좌 못찾았다."));
        String account = bankAccount.getAccountNumber();
        String accountPassword = bankAccount.getAccountPassword();

        params.put("organization", "0004");
        params.put("startDate", "20241021");
        params.put("endDate", "20241028");
        params.put("orderBy", "0");
        params.put("inquiryType", "0");
        params.put("account", account);
        params.put("accountPassword", accountPassword);
        params.put("birthDate", loggedInUser.getBirthDate());
        params.put("connectedId", connectedInfoByUser.getConnectedId());

        String result = easyCodef.requestProduct("/v1/kr/bank/p/account/transaction-list", EasyCodefServiceType.DEMO, params);
        return ResponseEntity.ok(result);
    }

}
