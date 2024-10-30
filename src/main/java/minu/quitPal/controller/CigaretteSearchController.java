package minu.quitPal.controller;

import lombok.RequiredArgsConstructor;
import minu.quitPal.dto.TransactionItemsDto;
import minu.quitPal.service.CigaretteService;
import minu.quitPal.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CigaretteSearchController {

    private final CigaretteService cigaretteService;
    private final TransactionService transactionService;

    @PostMapping("/search/cigarette")
    public ResponseEntity<?> searchCigaretteName(@RequestBody TransactionItemsDto transactionItemsDto) {
        if (!cigaretteService.containingCigarette(transactionItemsDto)) {
            return ResponseEntity.status(HttpStatus.OK).body(transactionService.checkTransaction(transactionItemsDto.getTransactionId()));
        } else return ResponseEntity.status(HttpStatus.ACCEPTED).body("품목에 담배가 있어서 안됩니다.");
    }

}
