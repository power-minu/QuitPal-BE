package minu.quitPal.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
@RequestMapping("/csv")
public class MapoCigaretteStore {

    @PostMapping("/store")
    public ResponseEntity<?> csvInput() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/qp-demo?allowLoadLocalInfile=true";
        String username = "root";
        String password = "root";
        String csvFilePath = "/Users/minu/Desktop/workspace/ocr-demo/mapo_cigarette_csv.csv";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {

            String line;
            int columnCount = 0;

            // 첫 번째 줄에서 필드 개수 확인
            if ((line = br.readLine()) != null) {
                columnCount = line.split(",").length; // 필드 개수
            }

            // 테이블 생성 쿼리 작성
            StringBuilder createTableQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS mapo_cigarette_store (");
            for (int i = 1; i <= columnCount; i++) {
                createTableQuery.append("column" + i + " VARCHAR(255)");
                if (i < columnCount) {
                    createTableQuery.append(", ");
                }
            }
            createTableQuery.append(");");

            // 테이블 생성
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createTableQuery.toString());
                System.out.println("Table created successfully.");
            }

            // LOAD DATA INFILE 명령어로 데이터 삽입
            String loadDataQuery = "LOAD DATA local INFILE '" + csvFilePath + "' INTO TABLE mapo_cigarette_store " +
                    "FIELDS TERMINATED BY ',' " +
                    "LINES TERMINATED BY '\\n' " +
                    "IGNORE 1 ROWS;"; // 첫 번째 줄은 헤더로 무시

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(loadDataQuery);
                System.out.println("Data inserted successfully.");
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("삽입 완료");
    }

}
