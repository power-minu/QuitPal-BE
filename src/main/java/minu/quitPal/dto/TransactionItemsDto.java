package minu.quitPal.dto;

import lombok.Data;

import java.util.List;

@Data
public class TransactionItemsDto {

    private Long transactionId;
    private List<String> items;

}
