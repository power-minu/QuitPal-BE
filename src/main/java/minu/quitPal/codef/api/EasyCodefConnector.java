package minu.quitPal.codef.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import minu.quitPal.entity.AccessToken;
import minu.quitPal.repository.AccessTokenRepository;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class EasyCodefConnector {

    private final ObjectMapper mapper;
    private final int REPEAT_COUNT = 3;

    private final AccessTokenRepository accessTokenRepository;

    /**
     * Desc : CODEF 상품 조회 요청
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:35:26 PM
     * @param urlPath
     * @param serviceType
     * @param bodyMap
     * @param properties
     * @return
     * @throws InterruptedException
     */
    @SuppressWarnings("unchecked")
    public EasyCodefResponse execute(
            String urlPath,
            int serviceType,
            HashMap<String, Object> bodyMap,
            EasyCodefProperties properties
    ) throws InterruptedException, IOException {
        //	#1.토큰 체크
        String domain;
        String clientId;
        String clientSecret;

        if(serviceType == 1) {
            domain = EasyCodefConstant.DEMO_DOMAIN;
            clientId = properties.getDemoClientId();
            clientSecret = properties.getDemoClientSecret();
        } else {
            domain = EasyCodefConstant.SANDBOX_DOMAIN;
            clientId = EasyCodefConstant.SANDBOX_CLIENT_ID;
            clientSecret = EasyCodefConstant.SANDBOX_CLIENT_SECRET;
        }

        String accessToken = getToken(clientId, clientSecret); // 토큰 반환

        //	#2.요청 파라미터 인코딩
        String bodyString;
        try {
            bodyString = mapper.writeValueAsString(bodyMap);
            bodyString = URLEncoder.encode(bodyString, "UTF-8");
        } catch (JsonProcessingException e) {
            return new EasyCodefResponse(EasyCodefMessageConstant.INVALID_JSON);
        } catch (UnsupportedEncodingException e) {
            return new EasyCodefResponse(EasyCodefMessageConstant.UNSUPPORTED_ENCODING);
        }

        //	#3.상품 조회 요청
        HashMap<String, Object> responseMap = requestProduct(domain + urlPath, accessToken, bodyString);
        if(EasyCodefConstant.INVALID_TOKEN.equals(responseMap.get("error")) || "CF-00401".equals(((HashMap<String, Object>)responseMap.get(EasyCodefConstant.RESULT)).get(EasyCodefConstant.CODE))){	// 액세스 토큰 유효기간 만료되었을 경우 토큰 재발급 후 상품 조회 요청 진행
            EasyCodefTokenMap.setToken(clientId, null);		// 토큰 정보 초기화
            accessToken = getToken(clientId, clientSecret); // 토큰 설정
            responseMap = requestProduct(domain + urlPath, accessToken, bodyString);
        } else if (EasyCodefConstant.ACCESS_DENIED.equals(responseMap.get("error")) || "CF-00403".equals(((HashMap<String, Object>)responseMap.get(EasyCodefConstant.RESULT)).get(EasyCodefConstant.CODE))) {	// 접근 권한이 없는 경우 - 오류코드 반환
            EasyCodefResponse response = new EasyCodefResponse(EasyCodefMessageConstant.UNAUTHORIZED, EasyCodefConstant.ACCESS_DENIED);
            return response;
        }

        //	#4.상품 조회 결과 반환
        return new EasyCodefResponse(responseMap);
    }

    /**
     * Desc : CODEF HTTP POST 요청
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:35:34 PM
     * @param urlPath
     * @param token
     * @param bodyString
     * @return
     */
    private HashMap<String, Object> requestProduct(String urlPath, String token, String bodyString) {
        BufferedReader br = null;
        try {
            // HTTP 요청을 위한 URL 오브젝트 생성
            URL url = new URL(urlPath);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept", "application/json");

            if (token != null && !"".equals(token)) {
                con.setRequestProperty("Authorization", "Bearer " + token);		// 엑세스 토큰 헤더 설정
            }

            // 리퀘스트 바디 전송
            OutputStream os = con.getOutputStream();
            if (bodyString != null && !"".equals(bodyString)) {
                os.write(bodyString.getBytes());
            }
            os.flush();
            os.close();

            // 응답 코드 확인
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                EasyCodefResponse response = new EasyCodefResponse(EasyCodefMessageConstant.BAD_REQUEST, urlPath);
                return response;
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                EasyCodefResponse response = new EasyCodefResponse(EasyCodefMessageConstant.UNAUTHORIZED, urlPath);
                return response;
            } else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                EasyCodefResponse response = new EasyCodefResponse(EasyCodefMessageConstant.FORBIDDEN, urlPath);
                return response;
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                EasyCodefResponse response = new EasyCodefResponse(EasyCodefMessageConstant.NOT_FOUND, urlPath);
                return response;
            } else {
                EasyCodefResponse response = new EasyCodefResponse(EasyCodefMessageConstant.SERVER_ERROR, urlPath);
                return response;
            }

            // 응답 바디 read
            String inputLine;
            StringBuffer responseStr = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                responseStr.append(inputLine);
            }
            br.close();

            // 결과 반환
            return mapper.readValue(URLDecoder.decode(responseStr.toString(), "UTF-8"), new TypeReference<HashMap<String, Object>>(){});
        } catch (Exception e) {
            return new EasyCodefResponse(EasyCodefMessageConstant.LIBRARY_SENDER_ERROR, e.getMessage());
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException ignored) {}
            }
        }
    }

    /**
     * Desc : 엑세스 토큰 반환
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:35:47 PM
     * @param clientId
     * @param clientSecret
     * @return
     * @throws InterruptedException
     */
    private String getToken(String clientId, String clientSecret) throws InterruptedException, IOException {
        int i = 0;
//        String accessToken = EasyCodefTokenMap.getToken(clientId);
        Optional<AccessToken> accessToken = accessTokenRepository.findAccessTokenByClientIdOrderByIdDesc(clientId);
        if(accessToken.isEmpty() || "".equals(accessToken.get().getToken()) || !checkToken(accessToken.get().getToken())) { //만료 조건 추가
            while(i < REPEAT_COUNT) {	// 토큰 발급 요청은 최대 3회까지 재시도
                HashMap<String, Object> tokenMap = publishToken(clientId, clientSecret);	// 토큰 발급 요청
                if(tokenMap != null) {
                    AccessToken newToken = AccessToken.builder()
                            .clientId(clientId)
                            .token((String)tokenMap.get("access_token"))
                            .build();
                    // EasyCodefTokenMap.setToken(clientId, newToken);	// 토큰 저장
                    accessTokenRepository.save(newToken);
                    accessToken = Optional.of(newToken);
                }

                if(accessToken.isPresent() || !"".equals(accessToken.get().getToken())) {
                    break;	// 정상 발급시 반복문 종료
                }

                Thread.sleep(20);
                i++;
            }
        }

        return accessToken.get().getToken();
    }

    /**
     * Desc : CODEF 엑세스 토큰 발급 요청
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:36:01 PM
     * @param clientId
     * @param clientSecret
     * @return
     */
    protected HashMap<String, Object> publishToken(
            String clientId,
            String clientSecret
    ) throws IOException {
        BufferedReader br = null;
        try {
            // HTTP 요청을 위한 URL 오브젝트 생성
            URL url = new URL(EasyCodefConstant.OAUTH_DOMAIN + EasyCodefConstant.GET_TOKEN);
            String params = "grant_type=client_credentials&scope=read";	// Oauth2.0 사용자 자격증명 방식(client_credentials) 토큰 요청 설정

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // 클라이언트아이디, 시크릿코드 Base64 인코딩
            String auth = clientId + ":" + clientSecret;
            byte[] authEncBytes = Base64.getEncoder().encode(auth.getBytes());
            String authStringEnc = new String(authEncBytes);
            String authHeader = "Basic " + authStringEnc;

            con.setRequestProperty("Authorization", authHeader);
            con.setDoInput(true);
            con.setDoOutput(true);

            // 리퀘스트 바디 전송
            OutputStream os = con.getOutputStream();
            os.write(params.getBytes());
            os.flush();
            os.close();

            // 응답 코드 확인
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {	// 정상 응답
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {	 // 에러 발생
                return null;
            }

            // 응답 바디 read
            String inputLine;
            StringBuffer responseStr = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                responseStr.append(inputLine);
            }
            br.close();
            HashMap<String, Object> tokenMap = mapper.readValue(URLDecoder.decode(responseStr.toString(), "UTF-8"), new TypeReference<HashMap<String, Object>>(){});
            return tokenMap;
        } catch (Exception e) {
            System.out.println("e = " + e);
            return null;
        } finally {
            if(br != null) {
                br.close();
            }
        }
    }

    /**
     * 토큰 유효기간 확인
     * @param accessToken
     * @return
     */
    private static boolean checkToken(String accessToken) {
        HashMap<String, Object> tokenMap = null;
        try {
            tokenMap = EasyCodefUtil.getTokenMap(accessToken);
        } catch (IOException e) {
            // 확인 중 오류 발생 시
            return false;
        }
        // 토큰의 유효 기간 확인
        return EasyCodefUtil.checkValidity((int) (tokenMap.get("exp")));
    }

}
