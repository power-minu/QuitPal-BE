package minu.quitPal.entity.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    @Setter
    @Column(length = 1024)
    private String accountPassword;

    @ManyToOne
    private CodefConnectedId codefConnectedId;

    @Builder
    public BankAccount(String accountNumber, String accountPassword, CodefConnectedId codefConnectedId) {
        this.accountNumber = accountNumber;
        this.accountPassword = accountPassword;
        this.codefConnectedId = codefConnectedId;
    }

}
