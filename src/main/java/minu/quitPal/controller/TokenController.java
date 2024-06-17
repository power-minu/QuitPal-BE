package minu.quitPal.controller;

import lombok.RequiredArgsConstructor;
import minu.quitPal.service.CodefTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final CodefTokenService codefTokenService;

    @GetMapping("/token")
    public ResponseEntity<String> getToken() throws IOException {
        return ResponseEntity.ok(codefTokenService.getCodefAccessToken());
    }

}
