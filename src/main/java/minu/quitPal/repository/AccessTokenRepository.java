package minu.quitPal.repository;

import minu.quitPal.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    Optional<AccessToken> findAccessTokenByClientId(String clientId);

    Optional<AccessToken> findAccessTokenByClientIdOrderByIdDesc(String clientId);

}
