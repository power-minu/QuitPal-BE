package minu.quitPal.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/image")
public class ImageController {

    @PostMapping
    public String imageTest(@RequestBody ImageTestRequest imageTestRequest) {
        String image = imageTestRequest.getImage();

        // 파일 경로 설정 (예: 현재 프로젝트의 resources 디렉토리)
        Path path = Paths.get("src/main/resources/image.txt");

        try {
            // 이미지 문자열을 파일에 저장
            Files.write(path, image.getBytes());
            return "Image saved successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to save image: " + e.getMessage();
        }

    }

}
