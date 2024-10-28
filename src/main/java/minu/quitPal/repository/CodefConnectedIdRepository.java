package minu.quitPal.repository;

import minu.quitPal.entity.user.CodefConnectedId;
import minu.quitPal.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodefConnectedIdRepository extends JpaRepository<CodefConnectedId, Long> {
    Optional<CodefConnectedId> findCodefConnectedIdByUser(User user);
}
