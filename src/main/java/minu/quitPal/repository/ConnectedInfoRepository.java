package minu.quitPal.repository;

import minu.quitPal.entity.user.ConnectedInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectedInfoRepository extends JpaRepository<ConnectedInfo, Long> {
}
