package minu.quitPal.repository;

import minu.quitPal.dto.StoreSimilarity;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

@Component
public class StoreRepository {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/qp-demo"; // 데이터베이스 URL
    private static final String USER = "root"; // 데이터베이스 사용자 이름
    private static final String PASSWORD = "root"; // 데이터베이스 비밀번호

    public boolean jaccardExistsStoreName(String place) {
        List<StoreSimilarity> similarStores = findSimilarStores(place, 1); // 상위 1개 유사한 가게 이름
        for (StoreSimilarity store : similarStores) {
            System.out.printf("가게 이름: %s, 유사도: %.2f%n", store.getStoreName(), store.getSimilarity());
        }
        if (similarStores.isEmpty()) return false;
        else return similarStores.get(0).getSimilarity() >= 0.7;
    }

    private static List<StoreSimilarity> findSimilarStores(String searchQuery, int limit) {
        List<StoreSimilarity> similarStores = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            // 공백 제거 후 검색어를 2-gram으로 분할
            String cleanedQuery = searchQuery.replace(" ", "");
            Set<String> queryNGrams = generateNGrams(cleanedQuery, 2);

            // 2-gram 테이블에서 가게 이름별로 유사도 계산
            String sql = "SELECT store_name, ngram FROM store_name_2gram_table";
            Map<String, Set<String>> storeNGramsMap = new HashMap<>();

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String storeName = rs.getString("store_name");
                    String ngram = rs.getString("ngram");

                    storeNGramsMap
                            .computeIfAbsent(storeName, k -> new HashSet<>())
                            .add(ngram);
                }
            }

            // 유사도 계산
            for (Map.Entry<String, Set<String>> entry : storeNGramsMap.entrySet()) {
                String storeName = entry.getKey();
                Set<String> storeNGrams = entry.getValue();
                double similarity = calculateJaccardSimilarity(queryNGrams, storeNGrams);

                if (similarity > 0) {
                    similarStores.add(new StoreSimilarity(storeName, similarity));
                }
            }

            // 유사도 순으로 정렬
            similarStores.sort((a, b) -> Double.compare(b.getSimilarity(), a.getSimilarity()));

            // 상위 limit개 결과 반환
            if (similarStores.size() > limit) {
                return similarStores.subList(0, limit);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return similarStores;
    }

    // Jaccard Similarity 계산
    private static double calculateJaccardSimilarity(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }

    // n-gram 생성
    private static Set<String> generateNGrams(String text, int n) {
        Set<String> ngrams = new HashSet<>();
        for (int i = 0; i <= text.length() - n; i++) {
            ngrams.add(text.substring(i, i + n));
        }
        return ngrams;
    }
}
