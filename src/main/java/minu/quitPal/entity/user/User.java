package minu.quitPal.entity.user;

import jakarta.persistence.*;
import lombok.*;
import minu.quitPal.entity.Transaction;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;
    private String birthDate;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @CreationTimestamp
    @Setter
    private LocalDate joinDate;

    @Setter
    private String expoPushToken;

    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions = new ArrayList<>();

    /* ------------------- */

    @Builder
    public User(String email, String password, Authority authority, String birthDate) {
        this.email = email;
        this.password = password;
        this.authority = authority;
        this.birthDate = birthDate;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

}
