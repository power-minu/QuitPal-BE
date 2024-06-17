package minu.quitPal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessToken {

    @Id
    @GeneratedValue
    private long id;

    private String clientId;

    @Column(length = 5000)
    private String token;

    @Builder
    public AccessToken(String clientId, String token) {
        this.clientId = clientId;
        this.token = token;
    }
}
