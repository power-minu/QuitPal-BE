package minu.quitPal.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import minu.quitPal.entity.Transaction;

import java.time.LocalDate;

@Data
public class TransactionResponse {

    private String place;
    private int amount;
    private LocalDate purchaseDate;
    private boolean checked;
    private boolean expired;

    @Builder
    private TransactionResponse(String place, int amount, LocalDate purchaseDate, boolean checked, boolean expired) {
        this.place = place;
        this.amount = amount;
        this.purchaseDate = purchaseDate;
        this.checked = checked;
        this.expired = expired;
    }

    public static TransactionResponse from(Transaction transaction) {
        return TransactionResponse.builder()
                .place(transaction.getPlace())
                .amount(transaction.getAmount())
                .purchaseDate(transaction.getPurchaseDate())
                .checked(transaction.isChecked())
                .expired(transaction.isExpired())
                .build();
    }

}
