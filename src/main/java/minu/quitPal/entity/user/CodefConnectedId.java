package minu.quitPal.entity.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodefConnectedId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String connectedId;

    @Builder
    public CodefConnectedId(User user, String connectedId) {
        this.user = user;
        this.connectedId = connectedId;
    }

}
