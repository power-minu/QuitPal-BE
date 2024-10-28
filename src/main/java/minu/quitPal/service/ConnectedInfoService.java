package minu.quitPal.service;

import lombok.RequiredArgsConstructor;
import minu.quitPal.entity.user.CodefConnectedId;
import minu.quitPal.repository.CodefConnectedIdRepository;
import minu.quitPal.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConnectedInfoService {

    private final CodefConnectedIdRepository codefConnectedIdRepository;
    private final UserRepository userRepository;

    public CodefConnectedId createConnectedInfo(Long userId, String conId) {
        CodefConnectedId codefConnectedId = CodefConnectedId.builder()
                .connectedId(conId)
                .user(userRepository.findById(userId).get())
                .build();

        return codefConnectedIdRepository.save(codefConnectedId);
    }
}
