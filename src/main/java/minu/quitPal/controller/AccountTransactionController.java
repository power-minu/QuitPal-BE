package minu.quitPal.controller;

import lombok.RequiredArgsConstructor;
import minu.quitPal.codef.api.EasyCodef;
import minu.quitPal.codef.api.EasyCodefProperties;
import minu.quitPal.codef.api.EasyCodefServiceType;
import minu.quitPal.codef.api.EasyCodefUtil;
import minu.quitPal.dto.UserResponseDto;
import minu.quitPal.entity.user.ConnectedInfo;
import minu.quitPal.entity.user.User;
import minu.quitPal.repository.ConnectedInfoRepository;
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
public class AccountTransactionController {

    private final EasyCodef easyCodef;
    private final EasyCodefUtil easyCodefUtil;
    private final EasyCodefProperties properties;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ConnectedInfoRepository connectedInfoRepository;

    @PostMapping("/trans")
    public ResponseEntity<String> getAccountTransactionHistory(@RequestBody HashMap<String, Object> params) throws IOException, InterruptedException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        params.replace("accountPassword", easyCodefUtil.encryptRSA((String) params.get("accountPassword"), properties.getPublicKey()));

        UserResponseDto myInfoBySecurity = userService.getMyInfoBySecurity();
        Optional<User> loggedInUser = userRepository.findUserByEmail(myInfoBySecurity.getEmail());
        Optional<ConnectedInfo> connectedInfoByUser = connectedInfoRepository.findConnectedInfoByUser(loggedInUser.get());

        params.put("connectedId", connectedInfoByUser.get().getConnectedId());

        String result = easyCodef.requestProduct("/v1/kr/bank/p/account/transaction-list", EasyCodefServiceType.DEMO, params);
        return ResponseEntity.ok(result);
    }

}
