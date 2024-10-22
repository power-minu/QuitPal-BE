package minu.quitPal.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import minu.quitPal.entity.user.User;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Service
public class NotificationService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendNotification(User user, String message) {
        try {
            String expoPushToken = user.getExpoPushToken(); // 사용자의 Expo 푸시 토큰 가져오기

            URL url = new URL("https://exp.host/--/api/v2/push/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonPayload = objectMapper.writeValueAsString(Map.of(
                    "to", expoPushToken,
                    "title", "알림 제목",
                    "body", message
            ));

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                // 오류 처리
                System.err.println("푸시 알림 전송 실패: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
