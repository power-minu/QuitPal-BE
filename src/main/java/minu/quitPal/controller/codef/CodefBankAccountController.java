package minu.quitPal.controller.codef;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/codef/accounts")
public class CodefBankAccountController {

    @GetMapping
    public ResponseEntity<?> bankAccountList() {
        return ResponseEntity.ok("");
    }

}
