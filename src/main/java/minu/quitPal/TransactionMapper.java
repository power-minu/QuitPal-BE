package minu.quitPal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import minu.quitPal.entity.Transaction;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionMapper {

    public static List<Transaction> mapTransactions(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        JsonNode resTrHistoryList = rootNode.path("data").path("resTrHistoryList");
        List<Transaction> transactions = new ArrayList<>();

        for (JsonNode transactionNode : resTrHistoryList) {
            if (transactionNode.path("resAccountOut").asInt() > 0) {
                Transaction transaction = Transaction.builder()
                        .place(transactionNode.path("resAccountDesc3").asText())
                        .amount(transactionNode.path("resAccountOut").asInt())
                        .purchaseDate(LocalDate.parse(transactionNode.path("resAccountTrDate").asText(), DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .purchaseTime(LocalTime.parse(transactionNode.path("resAccountTrTime").asText(), DateTimeFormatter.ofPattern("HHmmss")))
                        .build();
                transactions.add(transaction);
            }
        }

        return transactions;
    }


}
