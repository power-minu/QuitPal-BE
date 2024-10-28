package minu.quitPal.controller.codef;

import lombok.RequiredArgsConstructor;
import minu.quitPal.codef.api.EasyCodef;
import minu.quitPal.codef.api.EasyCodefProperties;
import minu.quitPal.codef.api.EasyCodefServiceType;
import minu.quitPal.codef.api.EasyCodefUtil;
import minu.quitPal.entity.user.BankAccount;
import minu.quitPal.entity.user.User;
import minu.quitPal.repository.BankAccountRepository;
import minu.quitPal.repository.UserRepository;
import minu.quitPal.util.SecurityUtil;
import minu.quitPal.entity.user.CodefConnectedId;
import minu.quitPal.service.ConnectedInfoService;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/codef/connected-id")
public class ConnectedIdController {

    private final EasyCodef easyCodef;
    private final EasyCodefUtil easyCodefUtil;
    private final EasyCodefProperties properties;
    private final ConnectedInfoService connectedInfoService;
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    @PostMapping
    public ResponseEntity<String> createConnectedId(
            @RequestBody HashMap<String, Object> accountInfo
    ) throws IOException, InterruptedException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException, ParseException {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        User curUser = userRepository.findById(currentUserId).orElseThrow(() -> new RuntimeException("유저못찾음."));

        String accountNumber = (String) accountInfo.get("accountNumber");
        String accountPassword = (String) accountInfo.get("accountPassword");
        accountInfo.remove("accountNumber");
        accountInfo.remove("accountPassword");

        accountInfo.put("birthDate", curUser.getBirthDate());
        accountInfo.replace("password", easyCodefUtil.encryptRSA((String) accountInfo.get("password"), properties.getPublicKey()));
        List<HashMap<String, Object>> accountList = new ArrayList<>();
        accountList.add(accountInfo);

        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountList", accountList);

        String responseAccount = easyCodef.createAccount(EasyCodefServiceType.DEMO, requestMap);

        JSONParser jsonParser = new JSONParser();
        Object parsed = jsonParser.parse(responseAccount);
        JSONObject jsonObject = (JSONObject) parsed;
        String s = jsonObject.get("data").toString();

        Object parsedData = jsonParser.parse(s);
        JSONObject parsedDataToJSON = (JSONObject) parsedData;
        String conId = parsedDataToJSON.get("connectedId").toString();

        CodefConnectedId codefConnectedId = connectedInfoService.createConnectedInfo(currentUserId, conId);

        BankAccount bankAccount = BankAccount.builder()
                .accountNumber(accountNumber)
                .accountPassword(easyCodefUtil.encryptRSA(accountPassword, properties.getPublicKey()))
                .codefConnectedId(codefConnectedId)
                .build();
        bankAccountRepository.save(bankAccount);

        return ResponseEntity.ok(responseAccount);

    }

    @DeleteMapping
    public ResponseEntity<String> deleteConnectedId(@RequestBody HashMap<String, Object> accountInfo) throws IOException, InterruptedException {
        HashMap<String, Object> requestMap = new HashMap<>();
        List<HashMap<String, Object>> accountList = (List<HashMap<String, Object>>) accountInfo.get("accountList");

        requestMap.put("accountList", accountList);
        requestMap.put("connectedId", accountInfo.get("connectedId"));

        return ResponseEntity.ok(easyCodef.deleteAccount(EasyCodefServiceType.DEMO, requestMap));
    }

}
