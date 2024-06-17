package minu.quitPal.service;

import lombok.RequiredArgsConstructor;
import minu.quitPal.entity.user.ConnectedInfo;
import minu.quitPal.repository.ConnectedInfoRepository;
import minu.quitPal.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConnectedInfoService {

    private final ConnectedInfoRepository connectedInfoRepository;
    private final UserRepository userRepository;

    public ConnectedInfo createConnectedInfo(Long userId, String conId) {
        ConnectedInfo connectedInfo = ConnectedInfo.builder()
                .connectedId(conId)
                .user(userRepository.findById(userId).get())
                .build();

        return connectedInfoRepository.save(connectedInfo);
    }
}
