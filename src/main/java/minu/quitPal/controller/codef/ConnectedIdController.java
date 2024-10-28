package minu.quitPal.controller.codef;

import lombok.RequiredArgsConstructor;
import minu.quitPal.codef.api.EasyCodef;
import minu.quitPal.codef.api.EasyCodefProperties;
import minu.quitPal.codef.api.EasyCodefServiceType;
import minu.quitPal.codef.api.EasyCodefUtil;
import minu.quitPal.util.SecurityUtil;
import minu.quitPal.entity.user.ConnectedInfo;
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

    @PostMapping
    public ResponseEntity<String> createConnectedId(
            @RequestBody HashMap<String, Object> accountInfo
    ) throws IOException, InterruptedException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException, ParseException {
        Long currentUserId = SecurityUtil.getCurrentUserId();

        accountInfo.replace("password", easyCodefUtil.encryptRSA((String) accountInfo.get("password"), properties.getPublicKey()));

        List<HashMap<String, Object>> accountList = new ArrayList<>();
        accountList.add(accountInfo);

        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("accountList", accountList);

        System.out.println("requestMap.toString() = " + requestMap.toString());

        String responseAccount = easyCodef.createAccount(EasyCodefServiceType.DEMO, requestMap);

        System.out.println("responseAccount = " + responseAccount);
        JSONParser jsonParser = new JSONParser();
        Object parsed = jsonParser.parse(responseAccount);
        JSONObject jsonObject = (JSONObject) parsed;
        String s = jsonObject.get("data").toString();

        Object parsedData = jsonParser.parse(s);
        JSONObject parsedDataToJSON = (JSONObject) parsedData;
        String conId = parsedDataToJSON.get("connectedId").toString();

        System.out.println("currentUserId = " + currentUserId);
        System.out.println("conId = " + conId);

        ConnectedInfo connectedInfo = connectedInfoService.createConnectedInfo(currentUserId, conId);

        System.out.println("connectedInfo.getId() = " + connectedInfo.getId());

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
