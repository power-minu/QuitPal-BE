package minu.quitPal.controller;

import lombok.RequiredArgsConstructor;
import minu.quitPal.dto.TransactionResponse;
import minu.quitPal.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/my")
    public ResponseEntity<List<TransactionResponse>> myTransactionList() {
        return ResponseEntity.ok(transactionService.getMyTransactionList());
    }

}
