package minu.quitPal.service;

import lombok.RequiredArgsConstructor;
import minu.quitPal.dto.TransactionItemsDto;
import minu.quitPal.repository.CigaretteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CigaretteService {

    private final CigaretteRepository cigaretteRepository;

    public boolean containingCigarette(TransactionItemsDto transactionItemsDto) {
        for (String item : transactionItemsDto.getItems())
            if (cigaretteRepository.jaccardExistsCigaretteName(item)) return true;
        return false;
    }

}
