package minu.quitPal.codef.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class EasyCodefProperties {

    //	데모 엑세스 토큰 발급을 위한 클라이언트 아이디
    @Value("${oauth2.token.client-id}")
    private String demoClientId;

    //	데모 엑세스 토큰 발급을 위한 클라이언트 시크릿
    @Value("${oauth2.token.client-secret}")
    private String demoClientSecret;

    /**
     * -- SETTER --
     *  Desc : 데모 접속 토큰 설정
     *
     * @param demoAccessToken
     */
    //	OAUTH2.0 데모 토큰
    @Setter
    private String demoAccessToken = "";

    //----------------------------------------------//
//
//    //	정식 엑세스 토큰 발급을 위한 클라이언트 아이디
//    private String clientId = "";
//
//    //	정식 엑세스 토큰 발급을 위한 클라이언트 시크릿
//    private String clientSecret = "";
//
//    /**
//     * -- SETTER --
//     *  Desc : API 접속 토큰 설정
//     *
//     * @param accessToken
//     */
//    //	OAUTH2.0 토큰
//    @Setter
//    private String accessToken = "";
    //----------------------------------------------//

    /**
     * -- SETTER --
     *  Desc : RSA암호화를 위한 퍼블릭키 설정
     *
     * @param publicKey
     */
    //	RSA암호화를 위한 퍼블릭키
    @Value("${rsa.pubkey}")
    private String publicKey;

    /**
     * Desc : 데모서버 사용을 위한 클라이언트 정보 설정
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:37:10 PM
     * @param demoClientId
     * @param demoClientSecret
     */
    public void setClientInfoForDemo(String demoClientId, String demoClientSecret) {
        this.demoClientId = demoClientId;
        this.demoClientSecret = demoClientSecret;
    }

    /**
     * Desc : 데모 접속 토큰 반환
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:37:30 PM
     * @return
     */
    public String getDemoAccessToken() {
        return demoAccessToken;
    }

    /**
     * Desc : RSA암호화를 위한 퍼블릭키 반환
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:37:59 PM
     * @return
     */
    public String getPublicKey() {
        return publicKey;
    }

}
