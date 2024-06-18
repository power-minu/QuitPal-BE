package minu.quitPal.repository;

import minu.quitPal.entity.user.ConnectedInfo;
import minu.quitPal.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConnectedInfoRepository extends JpaRepository<ConnectedInfo, Long> {
    Optional<ConnectedInfo> findConnectedInfoByUser(User user);
}
