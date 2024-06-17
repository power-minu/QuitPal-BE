package minu.quitPal.service;

import lombok.RequiredArgsConstructor;
import minu.quitPal.codef.api.EasyCodef;
import minu.quitPal.codef.api.EasyCodefServiceType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class CodefTokenService {

    private final EasyCodef easyCodef;

    public String getCodefAccessToken() throws IOException {
        return easyCodef.requestToken(EasyCodefServiceType.DEMO);
    }

}
