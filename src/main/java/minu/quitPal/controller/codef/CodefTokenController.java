package minu.quitPal.controller.codef;

import lombok.RequiredArgsConstructor;
import minu.quitPal.service.CodefTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class CodefTokenController {

    private final CodefTokenService codefTokenService;

    @GetMapping("/codef/token")
    public ResponseEntity<String> getToken() throws IOException {
        return ResponseEntity.ok(codefTokenService.getCodefAccessToken());
    }

}
